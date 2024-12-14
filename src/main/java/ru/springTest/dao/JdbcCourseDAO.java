package ru.springTest.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Types;
import java.util.List;

@Repository("courseDAO")
public class JdbcCourseDAO implements CourseDAO {

    private static final String SQL_SELECT_COURSE =
            "SELECT id, title, length, description FROM courses";

    private static final String SQL_SELECT_COURSE_BY_ID =
            SQL_SELECT_COURSE + " WHERE id = ?";

    private static final String SQL_SELECT_COURSE_BY_TITLE =
            SQL_SELECT_COURSE + " WHERE title LIKE ?";

    private static final String SQL_DELETE_COURSE_BY_ID =
            "DELETE FROM courses WHERE id = ?";

    private static final String SQL_INSERT_COURSE =
            "INSERT INTO courses (title, length, description) VALUES (?, ?, ?)";

    private static final String SQL_UPDATE_COURSE =
            "UPDATE courses SET title = ?, length = ?, description = ? WHERE id = ?";


    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Course findById(int id) {
        return (Course) getJdbcTemplate().queryForObject(SQL_SELECT_COURSE_BY_ID,
                new BeanPropertyRowMapper(Course.class),
                id
        );
    }

    @Override
    public List<Course> findAll() {
        return getJdbcTemplate().query(SQL_SELECT_COURSE,
                new BeanPropertyRowMapper(Course.class));

    }

    @Override
    public List<Course> findByTitle(String title) {
        return getJdbcTemplate().query(SQL_SELECT_COURSE_BY_TITLE,
                new BeanPropertyRowMapper(Course.class),
                "%" + title + "%"
        );
    }

    @Override
    public void insert(Course course) {
        PreparedStatementCreatorFactory f =
                new PreparedStatementCreatorFactory(SQL_INSERT_COURSE,
                        Types.NVARCHAR, Types.INTEGER, Types.NVARCHAR);

        f.setGeneratedKeysColumnNames("id");
        KeyHolder kh = new GeneratedKeyHolder();

        getJdbcTemplate().update(
                f.newPreparedStatementCreator(new Object[]{
                        course.getTitle(), course.getLength(), course.getDescription()}),
                kh);

        course.setId(kh.getKey().intValue());

    }

    @Override
    public void update(Course course) {
        getJdbcTemplate().update(SQL_UPDATE_COURSE,
                course.getTitle(), course.getLength(),
                course.getDescription(), course.getId());

    }

    @Override
    public void delete(int id) {
        getJdbcTemplate().update(SQL_DELETE_COURSE_BY_ID, id);
    }

}
