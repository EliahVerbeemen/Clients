package kdg.be.Managers;

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



