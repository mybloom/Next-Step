package core.db;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import next.model.User;

public class DataBase {

	private static Map<String, User> users = Maps.newHashMap();

	public static void addUser(User user) {
		users.put(user.getUserId(), user);
	}

	public static Optional<User> findUserById(String userId) {
		return Optional.ofNullable(users.get(userId));
	}

	public static Collection<User> findAll() {
		return users.values();
	}

	public static boolean existUser(String userId, String password) {
		User user = Optional.ofNullable(users.get(userId))
			.orElseThrow(() -> new NoSuchElementException("등록된 회원이 아닙니다"));

		if (!user.getPassword().equals(password)) {
			throw new NoSuchElementException("비밀번호가 맞지 않습니다.");
		}

		return true;
	}
}
