/**
 * Kiosk API
 * 
 *
 * OpenAPI spec version: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.client.model;

import io.swagger.client.model.AddOrderResDataSoldOut;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class AddOrderResData  {
  
  @SerializedName("order_id")
  private String orderId = null;
  @SerializedName("soldOut")
  private List<AddOrderResDataSoldOut> soldOut = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getOrderId() {
    return orderId;
  }
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<AddOrderResDataSoldOut> getSoldOut() {
    return soldOut;
  }
  public void setSoldOut(List<AddOrderResDataSoldOut> soldOut) {
    this.soldOut = soldOut;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddOrderResData addOrderResData = (AddOrderResData) o;
    return (orderId == null ? addOrderResData.orderId == null : orderId.equals(addOrderResData.orderId)) &&
        (soldOut == null ? addOrderResData.soldOut == null : soldOut.equals(addOrderResData.soldOut));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (orderId == null ? 0: orderId.hashCode());
    result = 31 * result + (soldOut == null ? 0: soldOut.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddOrderResData {\n");
    
    sb.append("  orderId: ").append(orderId).append("\n");
    sb.append("  soldOut: ").append(soldOut).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
