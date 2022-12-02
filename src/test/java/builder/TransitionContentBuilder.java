package builder;

import model.TransitionIssue;

public class TransitionContentBuilder {
    private TransitionIssue transitionIssue;

    public String build(String transitionId){
        TransitionIssue.TransitionId id = new TransitionIssue.TransitionId(transitionId);
        transitionIssue = new TransitionIssue(id);
        return BodyJSONBuilder.getJSONContent(transitionIssue);
    }
}
