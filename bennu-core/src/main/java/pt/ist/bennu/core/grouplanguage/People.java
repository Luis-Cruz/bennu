package pt.ist.bennu.core.grouplanguage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.groups.PeopleGroup;
import pt.ist.bennu.core.domain.groups.PersistentGroup;

class People extends Group {
	private final List<String> usernames;

	public People(List<String> usernames) {
		this.usernames = usernames;
	}

	@Override
	public PersistentGroup group() {
		Set<User> users = new HashSet<>();
		for (String username : usernames) {
			User user = User.findByUsername(username);
			if (user != null) {
				users.add(user);
			}
		}
		return PeopleGroup.getInstance(users);
	}
}