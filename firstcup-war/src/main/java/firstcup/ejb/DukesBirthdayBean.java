/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package firstcup.ejb;

import firstcup.entity.FirstcupUser;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * DukesBirthdayBean is a stateless session bean that calculates the age
 * difference between a user and Duke, who was born on May 23, 1995.
 */
@Stateless
public class DukesBirthdayBean {

    private static final Logger logger
            = Logger.getLogger("firstcup.ejb.DukesBirthdayBean");

    @PersistenceContext
    private EntityManager em;

    public Double getAverageAgeDifference() {
        Double avgAgeDiff = (Double) em.createNamedQuery("findAverageAgeDifferenceOfAllFirstcupUsers").getSingleResult();
        logger.log(Level.INFO, "Average age difference is: {0}", avgAgeDiff);
        return avgAgeDiff;
    }

    public int getAgeDifference(Date date) {
        int ageDifference;
        Calendar theirBirthday = new GregorianCalendar();
        Calendar dukesBirthday = new GregorianCalendar(1995, Calendar.MAY, 23);

        // set the calendar object ot the passed in date
        theirBirthday.setTime(date);

        // subtract users age from dukes age
        ageDifference = dukesBirthday.get(Calendar.YEAR) - theirBirthday.get(Calendar.YEAR);
        logger.log(Level.INFO, "Raw ageDifference is: {0}", ageDifference);

        // check to see if dukes birthday occurs before users. if so
        // subtract one from the age difference
        if (dukesBirthday.before(theirBirthday) && (ageDifference > 0)) {
            ageDifference--;
        }

        // create and store the users bday in the database
        FirstcupUser user = new FirstcupUser(date, ageDifference);
        em.persist(user);

        logger.log(Level.INFO, "Final ageDifference is: {0}", ageDifference);

        return ageDifference;
    }
}
