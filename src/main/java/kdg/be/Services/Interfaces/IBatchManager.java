package kdg.be.Services.Interfaces;

import kdg.be.Modellen.Order;
//import kdg.be.Repositories.BatchRepository;

public interface IBatchManager {


    public void AddOrderToBatch(Order order);

    public void CloseBatch();


}
