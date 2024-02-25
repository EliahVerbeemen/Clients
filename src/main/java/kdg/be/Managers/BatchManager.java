package kdg.be.Managers;

import kdg.be.AfterTenOClockError;
import kdg.be.Controllers.CustomerController;
import kdg.be.Modellen.Order;
import kdg.be.Modellen.ProductState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

//Enkel bevestige orders toevoegen!
/*@Component
public class BatchManager implements IBatchManager {
Logger logger= LoggerFactory.getLogger(BatchManager.class);

    private BatchRepository batchRepository;


    public    BatchManager(BatchRepository batchRepository){

        this.batchRepository=batchRepository;

    }

    public void AddOrderToBatch(Order order){

        List<Batch> openBatches= batchRepository.findBatchByProductsStatusEnum(ProductState);
        if(openBatches.size()>0){

            openBatches.get(0).getOrdersInProcess().add(order);

        } else if (LocalTime.now().isBefore(LocalTime.of(22,0,0))) {
            Batch batch=new Batch(LocalDateTime.now());
            batch.getOrdersInProcess().add(order);
            batchRepository.save(batch);

        }
        else{
            LocalDateTime tommorow=LocalDateTime.of(LocalDate.now().plusDays(1),LocalTime.of(0,0));
            Batch batch=new Batch(tommorow);
            batchRepository.save(batch);
            throw new AfterTenOClockError();
        }

        }

    @Scheduled(cron = "22 0 * * * ?")
        public void CloseBatch(){
        List<Batch> openBatches= batchRepository.findBatchByProductsStatusEnum(ProductsStatusEnum.OPEN);
List<Batch> batchesToClose=openBatches.stream().filter(batch->batch.getCreateTime().toLocalDate().equals(LocalDate.now())&&batch.getProductState().equals(ProductsStatusEnum.OPEN)).toList();}}




*/



