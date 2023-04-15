package com.adda.mistry.booking.addamistrybookingservice.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "providers_detail")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderDetail implements Serializable {


    @Id
    private String id;

    private String name;
    private List<String> serviceLists;

    private List<String> addaAreas;

    private Long phonenumber;

//    @DocumentReference(lazy = true)
//    private IdProofs idProofs;

    private List<Address> address;

}
