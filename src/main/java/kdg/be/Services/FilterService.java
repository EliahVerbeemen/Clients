package kdg.be.Services;

import kdg.be.Modellen.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilterService {


    public List<Order> dateFilter(List<Order>orders, Optional<LocalDate>before,Optional<LocalDate>after){
        if(before.isPresent()){
            orders=orders.stream().filter(o->o.getOrderDate().isBefore(before.get())).collect(Collectors.toList());

        }
        if(after.isPresent()){

            orders=orders.stream().filter(o->o.getOrderDate().isAfter(after.get())).collect(Collectors.toList());

        }
        return orders;

    }



    public List<Order> productFilter(List<Order>orders,Optional<List<Long>>productIds){

        if(productIds.isPresent()){
List<Long>ids=productIds.get();
List<Long>toFilter=new ArrayList<>();
orders.forEach(o->{
List<Long> copyIds=new ArrayList<>(ids);
 Set<Long> keys=   o.getProducts().keySet();
    copyIds.retainAll(keys);
if(copyIds.size()==0){
    toFilter.add(o.getOrderId());

}
});
          orders=  orders.stream().filter(o->!toFilter.contains(o.getOrderId())).collect(Collectors.toList());
        }
return orders;
    }


    public List<Order> clientsFilter(List<Order>orders,Optional<List<Long>>clientIds){

        if(clientIds.isPresent()){
            List<Long>ids=clientIds.get();
          orders=  orders.stream().filter(o->ids.contains(o.getClient().getClientId())).collect(Collectors.toList());


        }
        return orders;
    }


}
