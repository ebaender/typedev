private synchronized void checkUsers() {
    for (User user : users) {
        if (user.getLastContact().getEpochSecond() + timeout < Instant.now().getEpochSecond()) {
            System.out.println(getClass() + " removing " + user.getName() + " after " + timeout + " seconds of contact loss");
            if (user.getSession() != null) {
                user.getSession().getUsers().remove(user);
            }
            users.remove(user);
        }
    }
}