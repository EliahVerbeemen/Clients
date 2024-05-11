package kdg.be.Modellen.DTO.ControllerDTO;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Enums.ClientType;

public class ClientDTO {
    public Long clientId;
    private int points;
    private ClientType clientType;

    public ClientDTO(Client client) {
        clientId = client.clientId;
        points = client.getPoints();
        clientType = client.getClientType();
    }
}
