package guru.springframework.spring6resttemplate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

// Para que Jackson sepa como mapear de la respuesta JSON a esta clase POJO, utilizamos anotaciones.
// Usamos la anotación @JsonIgnoreProperties para ignorar aquellas propiedades que vienen en el JSON, pero
// que no nos interesan.
// Me gustaría usar Generics en vez del tipo concreto BeerDTO, pero en BaseClientImpl.java, no es posible
// usar BeerDTOPageImpl<BeerDTO>.class
//
// Indicar también que, para que Jackson funcione y no haga LinkedHashMap, sino BeerDTO, hay que calificarlo,
// tal y como se indica en la parte PageImpl<guru.springframework.spring6resttemplate.model.BeerDTO> y en el constructor.
@JsonIgnoreProperties(ignoreUnknown = true, value = "pageable")
public class BeerDTOPageImpl<BeerDTO> extends PageImpl<guru.springframework.spring6resttemplate.model.BeerDTO> {

    // Con @JsonCreator le decimos a Jackson que use este constructor, y vamos a enlazar diferentes propiedades.
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BeerDTOPageImpl(@JsonProperty("content") List<guru.springframework.spring6resttemplate.model.BeerDTO> content,
                           @JsonProperty("number") int page,
                           @JsonProperty("size") int size,
                           @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page, size), total);
    }

    // Estos dos constructores se van a ignorar.
    public BeerDTOPageImpl(List<guru.springframework.spring6resttemplate.model.BeerDTO> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public BeerDTOPageImpl(List<guru.springframework.spring6resttemplate.model.BeerDTO> content) {
        super(content);
    }
}
