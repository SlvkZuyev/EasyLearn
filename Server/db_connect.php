<?php

class DB_CONNECT {

    function __construct() {
        $this->connect();
    }

    function __destruct() {
        $this->close();
    }

    function connect() {
        require 'db_config.php';

        $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die(mysql_error());

        //$db = mysql_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());

        return $con;
    }

    function close() {
        mysqli_close();
    }

}

?>