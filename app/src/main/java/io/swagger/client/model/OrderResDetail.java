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

import io.swagger.client.model.OrderResOption;
import java.math.BigDecimal;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class OrderResDetail  {
  
  @SerializedName("order_detail_id")
  private String orderDetailId = null;
  @SerializedName("detail_menu_id")
  private String detailMenuId = null;
  @SerializedName("detail_quantity")
  private Integer detailQuantity = null;
  @SerializedName("detail_file_id")
  private String detailFileId = null;
  @SerializedName("detail_menu_name")
  private String detailMenuName = null;
  @SerializedName("detail_price")
  private BigDecimal detailPrice = null;
  @SerializedName("detail_calory")
  private Integer detailCalory = null;
  @SerializedName("detail_points")
  private Integer detailPoints = null;
  @SerializedName("detail_description")
  private String detailDescription = null;
  @SerializedName("option")
  private List<OrderResOption> option = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getOrderDetailId() {
    return orderDetailId;
  }
  public void setOrderDetailId(String orderDetailId) {
    this.orderDetailId = orderDetailId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getDetailMenuId() {
    return detailMenuId;
  }
  public void setDetailMenuId(String detailMenuId) {
    this.detailMenuId = detailMenuId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getDetailQuantity() {
    return detailQuantity;
  }
  public void setDetailQuantity(Integer detailQuantity) {
    this.detailQuantity = detailQuantity;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getDetailFileId() {
    return detailFileId;
  }
  public void setDetailFileId(String detailFileId) {
    this.detailFileId = detailFileId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getDetailMenuName() {
    return detailMenuName;
  }
  public void setDetailMenuName(String detailMenuName) {
    this.detailMenuName = detailMenuName;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public BigDecimal getDetailPrice() {
    return detailPrice;
  }
  public void setDetailPrice(BigDecimal detailPrice) {
    this.detailPrice = detailPrice;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getDetailCalory() {
    return detailCalory;
  }
  public void setDetailCalory(Integer detailCalory) {
    this.detailCalory = detailCalory;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getDetailPoints() {
    return detailPoints;
  }
  public void setDetailPoints(Integer detailPoints) {
    this.detailPoints = detailPoints;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getDetailDescription() {
    return detailDescription;
  }
  public void setDetailDescription(String detailDescription) {
    this.detailDescription = detailDescription;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<OrderResOption> getOption() {
    return option;
  }
  public void setOption(List<OrderResOption> option) {
    this.option = option;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderResDetail orderResDetail = (OrderResDetail) o;
    return (orderDetailId == null ? orderResDetail.orderDetailId == null : orderDetailId.equals(orderResDetail.orderDetailId)) &&
        (detailMenuId == null ? orderResDetail.detailMenuId == null : detailMenuId.equals(orderResDetail.detailMenuId)) &&
        (detailQuantity == null ? orderResDetail.detailQuantity == null : detailQuantity.equals(orderResDetail.detailQuantity)) &&
        (detailFileId == null ? orderResDetail.detailFileId == null : detailFileId.equals(orderResDetail.detailFileId)) &&
        (detailMenuName == null ? orderResDetail.detailMenuName == null : detailMenuName.equals(orderResDetail.detailMenuName)) &&
        (detailPrice == null ? orderResDetail.detailPrice == null : detailPrice.equals(orderResDetail.detailPrice)) &&
        (detailCalory == null ? orderResDetail.detailCalory == null : detailCalory.equals(orderResDetail.detailCalory)) &&
        (detailPoints == null ? orderResDetail.detailPoints == null : detailPoints.equals(orderResDetail.detailPoints)) &&
        (detailDescription == null ? orderResDetail.detailDescription == null : detailDescription.equals(orderResDetail.detailDescription)) &&
        (option == null ? orderResDetail.option == null : option.equals(orderResDetail.option));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (orderDetailId == null ? 0: orderDetailId.hashCode());
    result = 31 * result + (detailMenuId == null ? 0: detailMenuId.hashCode());
    result = 31 * result + (detailQuantity == null ? 0: detailQuantity.hashCode());
    result = 31 * result + (detailFileId == null ? 0: detailFileId.hashCode());
    result = 31 * result + (detailMenuName == null ? 0: detailMenuName.hashCode());
    result = 31 * result + (detailPrice == null ? 0: detailPrice.hashCode());
    result = 31 * result + (detailCalory == null ? 0: detailCalory.hashCode());
    result = 31 * result + (detailPoints == null ? 0: detailPoints.hashCode());
    result = 31 * result + (detailDescription == null ? 0: detailDescription.hashCode());
    result = 31 * result + (option == null ? 0: option.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderResDetail {\n");
    
    sb.append("  orderDetailId: ").append(orderDetailId).append("\n");
    sb.append("  detailMenuId: ").append(detailMenuId).append("\n");
    sb.append("  detailQuantity: ").append(detailQuantity).append("\n");
    sb.append("  detailFileId: ").append(detailFileId).append("\n");
    sb.append("  detailMenuName: ").append(detailMenuName).append("\n");
    sb.append("  detailPrice: ").append(detailPrice).append("\n");
    sb.append("  detailCalory: ").append(detailCalory).append("\n");
    sb.append("  detailPoints: ").append(detailPoints).append("\n");
    sb.append("  detailDescription: ").append(detailDescription).append("\n");
    sb.append("  option: ").append(option).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
