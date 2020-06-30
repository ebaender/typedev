function syncProgress() {
    if (state === states.live_session) {
        $.post(synServlet, { key: authKey, progress: progress, mistakes: mistakes }, function (resp) {
            // might have changed by the time we have a response
            if (state === states.live_session) {
                resp = JSON.parse(resp);
                if (!jQuery.isEmptyObject(resp)) {
                    sessionProgress = resp.sessionProgress;
                    renderLiveSession(output);
                } else {
                    alert("sync failed.");
                    changeState(states.default);
                }
            }
        });
    }
}