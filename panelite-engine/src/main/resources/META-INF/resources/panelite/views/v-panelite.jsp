<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dev.maczkowski.panelite.engine.api.PaneliteMethodType" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>&#x1F525; Panelite &#x1F525; - ${applicationName}</title>
    <script>

        function isObject(item) {
            return (item && typeof item === 'object' && !Array.isArray(item));
        }

        function mergeDeep(target, source) {
            let output = Object.assign({}, target);
            if (isObject(target) && isObject(source)) {
                Object.keys(source).forEach(key => {
                    if (isObject(source[key])) {
                        if (!(key in target))
                            Object.assign(output, { [key]: source[key] });
                        else
                            output[key] = mergeDeep(target[key], source[key]);
                    } else {
                        Object.assign(output, { [key]: source[key] });
                    }
                });
            }
            return output;
        }

        function buildLeafObject(key, value) {
            var builder = value;
            const parts = key.split('_');
            parts.slice(1, parts.length).reverse().forEach(function (part) {
                const x = {};
                x[part] = builder;
                builder = x;
            });

            console.log(builder);

            return builder;
        }

        function prepareBody(entries) {
            const bodyObject = entries.map(function (e) {
                const key = e[0];
                const value = e[1];
                return buildLeafObject(key, value);
            }).reduce(function (a, b) {
                return mergeDeep(a, b);
            }, {});
            return JSON.stringify(bodyObject);
        }

        document.addEventListener('DOMContentLoaded', function () {
            Array.from(document.getElementsByTagName('form')).forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    let data;
                    try {
                        data = this;
                        const formData = new FormData(data);
                        const method = data.getAttribute('method');
                        document.getElementById('result_' + data.getAttribute('id')).style.display = 'none';
                        document.getElementById('error_' + data.getAttribute('id')).style.display = 'none';
                        document.getElementById('spinner_' + data.getAttribute('id')).style.display = 'inline-block';
                        document.getElementById('fire_' + data.getAttribute('id')).style.display = 'none';

                        fetch(data.getAttribute('action') + (method === 'GET' ? '?' + new URLSearchParams(formData) : ''), {
                            method: method,
                            body: method === 'POST' ? prepareBody(Array.from(formData.entries())) : undefined,
                            headers: method === 'POST' ? new Headers({'content-type': 'application/json'}) : undefined
                        }).then(function (res) {
                            return new Promise(function (resolve) {
                                res.text().then(function (text) {
                                    resolve({status: res.status, text: text})
                                });
                            });
                        }).then(function (result) {
                            document.getElementById('result_code_' + data.getAttribute('id')).textContent = result.status;
                            document.getElementById('result_message_' + data.getAttribute('id')).textContent = result.text;
                            document.getElementById('result_' + data.getAttribute('id')).style.display = 'block';
                        }).catch(reason => {
                            document.getElementById('error_' + data.getAttribute('id')).textContent = reason;
                            document.getElementById('error_' + data.getAttribute('id')).style.display = 'block';
                        }).finally(function () {
                            document.getElementById('spinner_' + data.getAttribute('id')).style.display = 'none';
                            document.getElementById('fire_' + data.getAttribute('id')).style.display = 'inline-block';
                        });
                    } catch(e) {
                        document.getElementById('error_' + data.getAttribute('id')).textContent = e;
                        document.getElementById('error_' + data.getAttribute('id')).style.display = 'block';
                        document.getElementById('spinner_' + data.getAttribute('id')).style.display = 'none';
                        document.getElementById('fire_' + data.getAttribute('id')).style.display = 'inline-block';
                    } finally {
                        event.preventDefault();
                    }
                });
            });
        });
    </script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #e5e5f7;
            background-image: linear-gradient(30deg, #f7caa3 12%, transparent 12.5%, transparent 87%, #f7caa3 87.5%, #f7caa3), linear-gradient(150deg, #f7caa3 12%, transparent 12.5%, transparent 87%, #f7caa3 87.5%, #f7caa3), linear-gradient(30deg, #f7caa3 12%, transparent 12.5%, transparent 87%, #f7caa3 87.5%, #f7caa3), linear-gradient(150deg, #f7caa3 12%, transparent 12.5%, transparent 87%, #f7caa3 87.5%, #f7caa3), linear-gradient(60deg, #f7caa377 25%, transparent 25.5%, transparent 75%, #f7caa377 75%, #f7caa377), linear-gradient(60deg, #f7caa377 25%, transparent 25.5%, transparent 75%, #f7caa377 75%, #f7caa377);
            background-size: 56px 98px;
            background-position: 0 0, 0 0, 28px 49px, 28px 49px, 0 0, 28px 49px;
        }

        article {
            opacity: initial;
            max-width: 800px;
            margin-left: auto;
            margin-right: auto;
            color: darkslategray;
        }

        .group {
            margin-top: 10px;
            margin-bottom: 10px;
            padding: 10px;
            background-color: #EE8225;
            border-radius: 5px;
        }

        .method {
            margin-top: 10px;
            margin-bottom: 10px;
            background-color: white;
        }

        .methodHeader {
            background-color: darkslategray;
        }

        .methodType {
            padding: 10px;
            background-color: white;
            display: inline-block;
            font-weight: bold;
        }

        .methodName {
            color: white;
            padding: 10px;
            display: inline-block;
        }

        .methodContent {
            padding: 10px;
        }

        ul {
            list-style-type: none;
            margin: 0px;
            padding-left: 0px;
        }

        ul.params {
            list-style-type: disc;
            padding-left: 20px;
        }

        ul.params > li {
            padding: 5px;
        }

        .title {
            text-align: center;
        }

        div.response {
            display: none;
            padding: 10px;
            margin-top: 10px;
            background-color: lightgrey;
        }

        div.error {
            display: none;
            padding: 10px;
            margin-top: 10px;
            background-color: lightpink;
        }

        .fire {
            height: 28px;
            width: 28px;
            display: inline-block;
        }

        .fire > button {
            width: inherit;
            height: inherit;
            border: none;
            border-radius: 50%;
            font-size: 17px;
        }

        /* spinner */
        .spinner {
            display: none;
            width:25px;
            height:25px;
            border-radius:50%;
            background:#EE8225;
            -webkit-mask:
                    repeating-conic-gradient(#0000 0deg,#000 1deg 70deg,#0000 71deg 90deg),
                    radial-gradient(farthest-side,#0000 calc(100% - 4.5px),#000 calc(100% - 4px));
            -webkit-mask-composite: destination-in;
            mask-composite: intersect;
            animation:s5 1s infinite ;
        }
        @keyframes s5 {to{transform: rotate(.5turn)}}

    </style>
</head>
<body>
<article>
    <h1 class="title">&#x1F525; Panelite &#x1F525;</h1>
    <h2 class="title">${applicationName}</h2>
    <ul>
        <c:forEach var="group" items="${groups}">
            <li class="group">
                <h3>${group.name}</h3>
                <ul>
                    <c:forEach var="method" items="${group.methods}">
                        <li>
                            <div class="method">
                                <div class="methodHeader">
                                    <div class="methodType">
                                        ${method.type}
                                    </div>
                                    <div class="methodName">
                                        ${method.name}
                                    </div>
                                </div>
                                <div class="methodContent">
                                    <form:form id="form_${group.name}_${method.name}"
                                               method="${method.type eq PaneliteMethodType.QUERY ? 'GET' : 'POST'}"
                                               action="/actuator/paneliteApi/groups/${group.name}/methods/${method.name}">
                                        <div>
                                            <t:params params="${method.params}"/>
                                        </div>
                                        <div id="fire_form_${group.name}_${method.name}" class="fire">
                                            <button type="submit">&#x1F525;</button>
                                                <%-- &#x1F525; = 🔥--%>
                                        </div>
                                        <div id="spinner_form_${group.name}_${method.name}" class="spinner">
                                            <div></div>
                                            <div></div>
                                            <div></div>
                                        </div>
                                    </form:form>
                                    <div id="error_form_${group.name}_${method.name}" class="error">
                                    </div>
                                    <div id="result_form_${group.name}_${method.name}" class="response">
                                        Response code:
                                        <p id="result_code_form_${group.name}_${method.name}"></p>
                                        Response message:
                                        <p id="result_message_form_${group.name}_${method.name}"></p>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </li>
        </c:forEach>
    </ul>
</article>

</body>
</html>
