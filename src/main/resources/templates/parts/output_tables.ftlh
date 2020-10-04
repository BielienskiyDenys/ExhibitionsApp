<#macro exhibition_search>

<#if searchResult??>
${searchResult}
<#else>
</#if>
            <#if exhibitions??>
                <#list exhibitions as exhibition>
                    <table class="table table-hover">
                        <tbody>
                        <tr>
                            <th scope="row">${exhibition.id}</th>
                            <td>
                                <p>
                                    <b>${exhibition.exNameEng}</b>
                                    <b>${exhibition.exNameNative}</b>
                                </p>
                                <p>
                                    <#list exhibition.hallScheduleList as hallSchedule>
                                        <span>Hall:&nbsp${hallSchedule.hallName}</span>&nbsp&nbsp&nbsp
                                        <span>Starts on: &nbsp${hallSchedule.startDateAsString}</span>&nbsp&nbsp&nbsp
                                        <span>Ends on:&nbsp${hallSchedule.endDateAsString}</span>
                                    </#list>
                                </p>
                                <p>
                                    Opening hours:

                                    <span>${exhibition.openTime}</span>
                                    -
                                    <span>${exhibition.closeTime}</span>
                                </p>
                                <p>
                                    <span>${exhibition.descriptionEng}</span>
                                </p>
                                <p>
                                    <span>${exhibition.descriptionNative}</span>
                                </p>
                                <p>
                                    <span>${exhibition.themesEng}</span>
                                    <span>${exhibition.themesNative}</span>
                                </p>
                                <p>
                                    Price:
                                    <span>${exhibition.ticketPrice}</span>
                                </p>
                                <#nested>
                                <form action="buy_ticket_admin" method="post" class="form-inline">
                                                                    <select size="1" name="ticketQuantity" id="ticketQuantity" class="form-control">
                                                                        <option value="1">1</option>
                                                                        <option value="2">2</option>
                                                                        <option value="3">3</option>
                                                                        <option value="4">4</option>
                                                                        <option value="5">5</option>
                                                                    </select>
                                                                    <input type="hidden" name="exhibitionId" value="${exhibition.id}"/>
                                                                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                                                    <button type="submit" class="btn btn-primary">Buy a ticket!</button>
                                                                </form>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </#list>
             <#else>
             </#if>

            <#if halls_with_exhibitions??>
                <#list halls_with_exhibitions as hall_with_exhibition>
                    <table class="table table-hover">
                        <tbody>
                        <tr>
                            <th scope="row">${hall_with_exhibition.exhibition.getId()}</th>
                            <td>
                                <p>
                                    <b>${hall_with_exhibition.exhibition.getExNameEng()}</b>
                                    <b>${hall_with_exhibition.exhibition.getExNameNative()}</b>
                                </p>
                                <p>
                                    <span>Hall:&nbsp${hall_with_exhibition.hallName}</span>&nbsp&nbsp&nbsp
                                    <span>Starts on: &nbsp${hall_with_exhibition.startDateAsString}</span>&nbsp&nbsp&nbsp
                                    <span>Ends on:&nbsp${hall_with_exhibition.endDateAsString}</span>
                                </p>
                                <p>
                                    Opening hours:
                                    <span>${hall_with_exhibition.exhibition.getOpenTime()}</span>
                                    -
                                    <span>${hall_with_exhibition.exhibition.getCloseTime()}</span>
                                </p>
                                <p>
                                    <span>${hall_with_exhibition.exhibition.getDescriptionEng()}</span>
                                </p>
                                <p>
                                    <span>${hall_with_exhibition.exhibition.getDescriptionNative()}</span>
                                </p>
                                <p>
                                    <span>${hall_with_exhibition.exhibition.getThemesEng()}</span>
                                    <span>${hall_with_exhibition.exhibition.getThemesNative()}</span>
                                </p>
                                <p>
                                    Price:
                                    <span>${hall_with_exhibition.exhibition.getTicketPrice()}</span>
                                </p>
                                <form action="buy_ticket_admin" method="post" class="form-inline">
                                    <select size="1" name="ticketQuantity" id="ticketQuantity" class="form-control">
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                    </select>
                                    <input type="hidden" name="exhibitionId" value="${hall_with_exhibition.exhibition.id}"/>
                                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                    <button type="submit" class="btn btn-primary">Buy a ticket!</button>
                                </form>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </#list>
                <#else>
              </#if>
</#macro>