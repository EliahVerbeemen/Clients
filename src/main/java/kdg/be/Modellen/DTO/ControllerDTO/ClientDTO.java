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
    }
}
