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

import io.swagger.client.model.LoginResData;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class LoginRes  {
  
  @SerializedName("responseStatus")
  private Integer responseStatus = null;
  @SerializedName("responseMsg")
  private String responseMsg = null;
  @SerializedName("data")
  private List<LoginResData> data = null;

  /**
   * 성공유부
   **/
  @ApiModelProperty(value = "성공유부")
  public Integer getResponseStatus() {
    return responseStatus;
  }
  public void setResponseStatus(Integer responseStatus) {
    this.responseStatus = responseStatus;
  }

  /**
   * 응답메시지
   **/
  @ApiModelProperty(value = "응답메시지")
  public String getResponseMsg() {
    return responseMsg;
  }
  public void setResponseMsg(String responseMsg) {
    this.responseMsg = responseMsg;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<LoginResData> getData() {
    return data;
  }
  public void setData(List<LoginResData> data) {
    this.data = data;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginRes loginRes = (LoginRes) o;
    return (responseStatus == null ? loginRes.responseStatus == null : responseStatus.equals(loginRes.responseStatus)) &&
        (responseMsg == null ? loginRes.responseMsg == null : responseMsg.equals(loginRes.responseMsg)) &&
        (data == null ? loginRes.data == null : data.equals(loginRes.data));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (responseStatus == null ? 0: responseStatus.hashCode());
    result = 31 * result + (responseMsg == null ? 0: responseMsg.hashCode());
    result = 31 * result + (data == null ? 0: data.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginRes {\n");
    
    sb.append("  responseStatus: ").append(responseStatus).append("\n");
    sb.append("  responseMsg: ").append(responseMsg).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}