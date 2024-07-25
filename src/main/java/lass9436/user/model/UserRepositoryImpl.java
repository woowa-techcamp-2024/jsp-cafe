package lass9436.user.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepositoryImpl implements UserRepository {

	private final List<User> users = new ArrayList<>();
	private final AtomicLong userSeqGenerator = new AtomicLong(1);

	@Override
	public User save(User user) {
		if (user.getUserSeq() == 0) {
			// 새로운 유저일 경우 userSeq 를 자동 증가시킴
			user.setUserSeq(userSeqGenerator.getAndIncrement());
		}

		users.stream()
			.filter(u -> u.getUserSeq() == (user.getUserSeq()))
			.findFirst()
			.ifPresentOrElse(
				existingUser -> {
					// 기존 유저가 존재할 경우, 인덱스를 찾아 업데이트
					int index = users.indexOf(existingUser);
					users.set(index, user);
				},
				() -> users.add(user)
			);
		return user;
	}

	@Override
	public User findByUserId(String userId) {
		return users.stream()
			.filter(u -> u.getUserId().equals(userId))
			.findFirst().orElse(null);
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(users);
	}

	@Override
	public void deleteByUserId(String userId) {
		users.removeIf(user -> user.getUserId().equals(userId));
	}

	@Override
	public User findByUserSeq(long userId) {
		return users.stream()
			.filter(u -> u.getUserSeq() == userId)
			.findFirst().orElse(null);
	}
}

