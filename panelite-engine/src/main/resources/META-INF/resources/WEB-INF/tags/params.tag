<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="params" required="true" type="java.util.List" %>
<%@ attribute name="prefix" required="false" type="java.lang.String" %>
<%@ tag import="dev.maczkowski.panelite.engine.api.PaneliteParamType" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<ul class="params">
    <c:forEach var="parameter" items="${params}">
        <li>
            <c:choose>
                <c:when test="${parameter.type == PaneliteParamType.COMPLEX}">
                    ${parameter.name}
                    <t:params params="${parameter.params}" prefix="${prefix}${parameter.name}_"/>
                </c:when>
                <c:otherwise>
                    <label>
                            ${parameter.name}
                            <c:choose>
                                <c:when test="${parameter.type == PaneliteParamType.INTEGER}">
                                    <input type="number" name="${prefix}${parameter.name}" />
                                </c:when>
                                <c:when test="${parameter.type == PaneliteParamType.DECIMAL}">
                                    <input type="number" name="${prefix}${parameter.name}" step="0.00000000001" />
                                </c:when>
                                <c:when test="${parameter.type == PaneliteParamType.BOOLEAN}">
                                    <select name="${prefix}${parameter.name}">
                                        <option value="false">False</option>
                                        <option value="true">True</option>
                                    </select>
                                </c:when>
                                <c:when test="${parameter.type == PaneliteParamType.TEXT}">
                                    <input name="${prefix}${parameter.name}" />
                                </c:when>
                            </c:choose>
                    </label>
                </c:otherwise>
            </c:choose>
        </li>
    </c:forEach>
</ul>
