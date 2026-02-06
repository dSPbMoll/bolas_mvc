package communications.dto;

import java.io.Serializable;

public class Frame implements Serializable {
    public final CommType header;
    public final PayloadComms payload;

    public Frame(CommType type, PayloadComms payload) {
        this.header = type;
        this.payload = payload;
    }
}
