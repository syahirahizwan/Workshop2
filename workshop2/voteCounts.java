package my.edu.utem.ftmk.workshop2;

public class voteCounts {

    String candidateName;

    public voteCounts () {

    }

    public voteCounts(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }
}
