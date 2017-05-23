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


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class MenuReq  {
  
  @SerializedName("store_id")
  private String storeId = null;
  @SerializedName("sub_category_id")
  private String subCategoryId = null;
  @SerializedName("orderBy")
  private String orderBy = null;
  @SerializedName("startNo")
  private Integer startNo = null;
  @SerializedName("cnt")
  private Integer cnt = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getStoreId() {
    return storeId;
  }
  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getSubCategoryId() {
    return subCategoryId;
  }
  public void setSubCategoryId(String subCategoryId) {
    this.subCategoryId = subCategoryId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getOrderBy() {
    return orderBy;
  }
  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getStartNo() {
    return startNo;
  }
  public void setStartNo(Integer startNo) {
    this.startNo = startNo;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getCnt() {
    return cnt;
  }
  public void setCnt(Integer cnt) {
    this.cnt = cnt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MenuReq menuReq = (MenuReq) o;
    return (storeId == null ? menuReq.storeId == null : storeId.equals(menuReq.storeId)) &&
        (subCategoryId == null ? menuReq.subCategoryId == null : subCategoryId.equals(menuReq.subCategoryId)) &&
        (orderBy == null ? menuReq.orderBy == null : orderBy.equals(menuReq.orderBy)) &&
        (startNo == null ? menuReq.startNo == null : startNo.equals(menuReq.startNo)) &&
        (cnt == null ? menuReq.cnt == null : cnt.equals(menuReq.cnt));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (storeId == null ? 0: storeId.hashCode());
    result = 31 * result + (subCategoryId == null ? 0: subCategoryId.hashCode());
    result = 31 * result + (orderBy == null ? 0: orderBy.hashCode());
    result = 31 * result + (startNo == null ? 0: startNo.hashCode());
    result = 31 * result + (cnt == null ? 0: cnt.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MenuReq {\n");
    
    sb.append("  storeId: ").append(storeId).append("\n");
    sb.append("  subCategoryId: ").append(subCategoryId).append("\n");
    sb.append("  orderBy: ").append(orderBy).append("\n");
    sb.append("  startNo: ").append(startNo).append("\n");
    sb.append("  cnt: ").append(cnt).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
