@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String key = req.getParameter("key");
    User user = users.get(key);
    JsonObject jsonResp = new JsonObject();
    if (user != null && user.getSession() != null) {
        jsonResp.addProperty(Standard.COD, user.getSession().getCode());
    }
    resp.getWriter().print(jsonResp);
    System.out.println(getClass() + " responded with " + jsonResp);
}