package org.ifpe.jakarta.main.request;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

@Setter
@XmlRootElement(name = "CandidateRequest")
public class CandidateRequest {

    private Integer partyNumber;
    private Integer candidateNumber;

    @XmlElement(name = "partyNumber")
    public Integer getPartyNumber() {
        return partyNumber;
    }

    @XmlElement(name = "candidateNumber")
    public Integer getCandidateNumber() {
        return candidateNumber;
    }
}
