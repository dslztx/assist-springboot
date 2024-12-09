package dubbo.api.domain;

import java.io.Serializable;

public class DubboServiceQuery implements Serializable {

    private static final long serialVersionUID = 3546509583245393480L;

    private Person person;

    public DubboServiceQuery(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {

        if (person == null) {
            return "Null";
        }

        return person.toString();
    }
}
