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
public class LoginResData  {
  
  @SerializedName("account_id")
  private String accountId = null;
  @SerializedName("name")
  private String name = null;
  @SerializedName("phone")
  private String phone = null;
  @SerializedName("status")
  private Integer status = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getAccountId() {
    return accountId;
  }
  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getStatus() {
    return status;
  }
  public void setStatus(Integer status) {
    this.status = status;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginResData loginResData = (LoginResData) o;
    return (accountId == null ? loginResData.accountId == null : accountId.equals(loginResData.accountId)) &&
        (name == null ? loginResData.name == null : name.equals(loginResData.name)) &&
        (phone == null ? loginResData.phone == null : phone.equals(loginResData.phone)) &&
        (status == null ? loginResData.status == null : status.equals(loginResData.status));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (accountId == null ? 0: accountId.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (phone == null ? 0: phone.hashCode());
    result = 31 * result + (status == null ? 0: status.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginResData {\n");
    
    sb.append("  accountId: ").append(accountId).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  phone: ").append(phone).append("\n");
    sb.append("  status: ").append(status).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}