package com.ecommerce.ecommerce.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class CreateAndUpdatedBy {

  private String createdBy;

  private String updatedBy;

}
