package kdg.be.Modellen.DTO.ControllerDTO;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Enums.ClientType;

public class ClientDTO {
    public Long clientId;
    private int points;
    private ClientType clientType;


    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ClientType getClientType() {
        return clientType;
    }

    private  String name;

    private String familyName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public ClientDTO() {
    }

    public ClientDTO(Long clientId, ClientType clientType) {
        this.clientId = clientId;
        this.clientType = clientType;

    }

    public ClientDTO(Client client) {
        clientId = client.clientId;
        points = client.getPoints();
        clientType = client.getClientType();
        this.email= client.getEmail();
        this.name= client.getName();
        this.familyName= client.getFamilyName();
    }
}
