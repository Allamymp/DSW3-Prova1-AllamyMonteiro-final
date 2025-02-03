package org.ifpe.jakarta.main.request;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

@Setter
@XmlRootElement(name = "vote")
public class VoteRequest {

    private Long partyId;
    private Long candidateId;

    @XmlElement(name = "partyId")
    public Long getPartyId() {
        return partyId;
    }

    @XmlElement(name = "candidateId")
    public Long getCandidateId() {
        return candidateId;
    }

}
