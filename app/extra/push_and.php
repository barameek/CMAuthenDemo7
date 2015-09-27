<?php
    $message = $_GET["message"];
    $url = 'https://api.parse.com/1/push';
    $appId = 'GHcfbRslwa4qgt5OhlsHDgkal1IF6wYObPnUBze3';
    $restKey = 'sBWHhhbTbUxOf19YdjvrUMxYDzWSADOtgunpXn6O';
    
    $push_payload = json_encode(array(
                                      "where" => array(
                                                       "appIdentifier" =>
                                                       "com.codemobiles.cmapppush",
                                                       "appVersion" =>
                                                       "1.0",
                                                       "channel" => "all"
                                                       ),
                                      "data" => array(
                                                      
                                                      "title" => "CodeMobile Inc.",
                                                      "description" => "Mobile Dev. is our passion", 
                                                      "action" => "com.codemobiles.cmapppush.UPDATE_STATUS",
                                                      )
                                      ));
    
    $rest = curl_init();
    curl_setopt($rest,CURLOPT_URL,$url);
    curl_setopt($rest,CURLOPT_PORT,443);
    curl_setopt($rest,CURLOPT_POST,1);
    curl_setopt($rest,CURLOPT_POSTFIELDS,$push_payload);
    curl_setopt($rest,CURLOPT_HTTPHEADER,
                array("X-Parse-Application-Id: " . $appId,
                      "X-Parse-REST-API-Key: " . $restKey,
                      "Content-Type: application/json"));
    
    $response = curl_exec($rest);
    echo $response;
    
    ?>
