package com.spring.service.user;

import com.spring.domain.user.Level;
import com.spring.domain.user.MockUserDao;
import com.spring.domain.user.User;
import com.spring.domain.user.UserDao;
import com.spring.service.TxProxyFactoryBean;
import com.spring.service.UserService;
import com.spring.service.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    private List<User> users;

    @Autowired
    private UserDao userDao;

    @Autowired
    MailSender mailSender;

    @Autowired
    UserService userService;
    @Autowired
    UserService testUserService;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    ApplicationContext context;

    @Test
    @DirtiesContext
    void upgradeAllOrNothing() throws Exception {
        userDao.deleteAll();
        users.forEach(user -> userDao.add(user));

        try {
            testUserService.upgradeLevels();
        } catch (IllegalArgumentException e) {
            System.out.println("hi");
        }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    public void upgradeLevels() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = new MailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "user1", Level.SILVER);
        checkUserAndLevel(updated.get(1), "user2", Level.GOLD);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(), is(2));
        assertThat(request.get(0), is(users.get(1).getEmail()));
        assertThat(request.get(1), is(users.get(3).getEmail()));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());

        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    static class TestUserServiceImpl extends UserServiceImpl {

        private String id = "madnite1";

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) {
                throw new IllegalArgumentException();
            }
            super.upgradeLevel(user);
        }
    }
    static class TestUserServiceException extends RuntimeException {}
}
