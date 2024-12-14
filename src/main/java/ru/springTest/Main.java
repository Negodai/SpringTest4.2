package ru.springTest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.springTest.configuration.DaoConfig;
import ru.springTest.dao.Course;
import ru.springTest.dao.CourseDAO;

public class Main {
    public static void main(String[] args) {


        try( AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(DaoConfig.class)) {

            CourseDAO courseDao = context.getBean(CourseDAO.class);

            for (Course cs : courseDao.findByTitle("WEB"))
                System.out.println(cs);


        }

    }
}
