@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String key = req.getParameter("key");
    User user = users.get(key);
    JsonObject jsonResp = new JsonObject();
    if (user != null && user.getSession() != null && user.getSession().isLive()) {
        String progress = req.getParameter("progress");
        String mistakes = req.getParameter("mistakes");
        if (progress != null) {
            try {
                user.setProgress(Integer.parseInt(progress));
                user.setMistakes(Integer.parseInt(mistakes));
                JsonArray sessionProgress = new JsonArray();
                for (User sessionUser : user.getSession().getUsers()) {
                    JsonObject userProgress = new JsonObject();
                    userProgress.addProperty("name", sessionUser.getName());
                    userProgress.addProperty("progress", sessionUser.getProgress());
                    userProgress.addProperty("mistakes", sessionUser.getMistakes());
                    sessionProgress.add(userProgress);
                }
                jsonResp.add(Standard.PRG, sessionProgress);
                user.getSession().think();
            } catch (NumberFormatException e) {
                System.out.println(getClass() + " could not parse progress string " + progress);
            }
        }
    }
    resp.getWriter().print(jsonResp);
    System.out.println(getClass() + " responded with " + jsonResp);
}