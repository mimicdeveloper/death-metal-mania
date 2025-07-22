package com.deathmetalmania.dao;

import java.util.ArrayList;
import java.util.List;

import com.deathmetalmania.exception.DaoException; // Import the DaoException
import com.deathmetalmania.model.UserProfile;
import com.deathmetalmania.model.dto.RegisterUserDto;
import com.deathmetalmania.model.dto.UserProfileDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.deathmetalmania.model.User;

@Component
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserProfile getUserProfile(String username) {
        UserProfile profile = null;

        String sql = "SELECT * " +
                     "FROM profile AS p " +
                     "JOIN users AS u ON p.user_id = u.user_id " +
                     "WHERE u.username = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);

        if (result.next()) {
            profile = new UserProfile(
                    result.getInt("profile_id"),
                    result.getInt("user_id"),
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getString("email")
            );
        }

        return profile;
    }

    @Override
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * " +
                     "FROM users " +
                     "WHERE user_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (Exception e) {
            throw new DaoException("Error retrieving user by ID", e);
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * " +
                     "FROM users " +
                     "ORDER BY username";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                User user = mapRowToUser(results);
                users.add(user);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (Exception e) {
            throw new DaoException("Error retrieving users list", e);
        }
        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null) {
            username = "";
        }
        User user = null;
        String sql = "SELECT * " +
                     "FROM users " +
                     "WHERE username = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (Exception e) {
            throw new DaoException("Error retrieving user by username", e);
        }
        return user;
    }

    @Override
    public User createUser(User newUser) {
        User user = null;
        String insertUserSql = "INSERT INTO users (username, password_hash, role) " +
                               "VALUES (?, ?, ?) " +
                               "RETURNING user_id";

        if (newUser.getHashedPassword() == null) {
            throw new DaoException("User cannot be created with null password");
        }
        try {
            String passwordHash = new BCryptPasswordEncoder().encode(newUser.getHashedPassword());

            Integer userId = jdbcTemplate.queryForObject(insertUserSql, int.class,
                    newUser.getUsername(), passwordHash, newUser.getRole());
            user = getUserById(userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation while creating user", e);
        } catch (Exception e) {
            throw new DaoException("Error creating user", e);
        }
        return user;
    }

    @Override
    public UserProfile getUserProfileByUsername(String username) {
        UserProfile profile = null;

        String sql = "SELECT * " +
                     "FROM profile AS p " +
                     "JOIN users AS u ON p.user_id = u.user_id " +
                     "WHERE username = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);

        if (result.next()) {
            profile = new UserProfile(
                    result.getInt("profile_id"),
                    result.getInt("user_id"),
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getString("email")
            );
        }

        return profile;
    }

    @Override
    public UserProfile updateUserProfile(int userId, UserProfileDto userProfile) {
        String sql = "UPDATE profile " +
                     "SET first_name = ?, last_name = ?, email = ? " +
                     "WHERE user_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, userProfile.getFirstname(),
                userProfile.getLastname(),
                userProfile.getEmail(),
                userId);

        return getUserProfileById(userId);
    }

    @Override
    public List<UserProfile> getAllUserProfiles() {
        List<UserProfile> profiles = new ArrayList<>();

        String sql = "SELECT * " +
                     "FROM profile AS p " +
                     "JOIN users AS u ON p.user_id = u.user_id " +
                     "ORDER BY u.username";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                UserProfile profile = new UserProfile(
                        results.getInt("profile_id"),
                        results.getInt("user_id"),
                        results.getString("first_name"),
                        results.getString("last_name"),
                        results.getString("email")
                );
                profiles.add(profile);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (Exception e) {
            throw new DaoException("Error retrieving all user profiles", e);
        }

        return profiles;
    }

    @Override
    public UserProfile getUserProfileById(int id) {
        UserProfile profile = null;
        String sql = "SELECT * " +
                     "FROM profile AS p " +
                     "JOIN users AS u ON p.user_id = u.user_id " +
                     "WHERE u.user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                profile = new UserProfile(
                        results.getInt("profile_id"),
                        results.getInt("user_id"),
                        results.getString("first_name"),
                        results.getString("last_name"),
                        results.getString("email")
                );
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (Exception e) {
            throw new DaoException("Error retrieving user profile by ID", e);
        }

        return profile;
    }

    @Override
    public void deleteUserById(int id) {
        String sql = "DELETE FROM users " +
                     "WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            throw new DaoException("Error deleting user by ID", e);
        }
    }

    @Override
    public User createUserWithProfile(RegisterUserDto dto) {
        if (!dto.isPasswordsMatch()) {
            throw new DaoException("Passwords do not match");
        }

        // Step 1: Create the user
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setHashedPassword(dto.getPassword());
        newUser.setRole(dto.getRole());

        User createdUser = createUser(newUser); // reuse existing logic

        // Step 2: Insert profile info
        String insertProfileSql = "INSERT INTO profile (user_id, first_name, last_name, email) " +
                "VALUES (?, ?, ?, ?)";

        try {
            jdbcTemplate.update(insertProfileSql,
                    createdUser.getId(),
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getEmail());
        } catch (Exception e) {
            throw new DaoException("Error creating user profile", e);
        }

        return createdUser;
    }



    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setHashedPassword(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
