<?php
class MySQL{

    public $pdo;
    private $currentConnection;

    public function __destruct() {
        $this->pdo = null;
    }

    /**
     * Connection a la base de donnée
     */
    public function __construct($db_driver=NULL, $db_hostname=NULL, $db_username=NULL, $db_password=NULL, $db_database=NULL) {

        // Recherche la connexion a utilisé
        switch (strtolower($db_driver)){
            case 'mysql':
                $connection = 'mysql:host='.$db_hostname.';port=3306;dbname='.$db_database;
                break;
            case 'pgsql':
                $connection = 'pgsql:host='.$db_hostname.' port=4444 dbname='.$db_database;
                break;
            case 'sqlite':
                $connection = 'sqlite:'.$db_hostname;
                break;
            case 'oci':
                $connection = 'OCI:'.$db_hostname;
                break;
        }

        try {
            $this->pdo = new PDO($connection, $db_username, $db_password, array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8'));
            $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->currentConnection = $connection;
        } catch(PDOException $e){

            switch ($e->getCode()) {
                case '1040':
                    die('D&eacute;sol&eacute;, nous sommes victime de notre succ&egrave;s. Trop de connexion sont d&eacute;j&agrave; ouverte. R&eacute;&eacute;say&eacute; plus tard.');
                    break;
                case '1044':
                    $contenu = 'Impossible de se connecter &agrave; la base de donn&eacute;e';
                    if (DB_DATABASE == 'password' || DB_DATABASE == 'databasename' || DB_USERNAME == 'username') {
                        $contenu .= '<br><small>Il doit y avoir un fichier init.php dans ' . __SITE_PATH . DS . 'includes, que vous n\'avez pas modifier</small>';
                    }
                    die($contenu);
                    break;
                case '1045':
                    $contenu = 'Impossible de se connecter &agrave; la base de donn&eacute;e';
                    if (DB_DATABASE == 'password' || DB_DATABASE == 'databasename' || DB_USERNAME == 'username') {
                        $contenu .= '<br><small>Il doit y avoir un fichier init.php dans ' . __SITE_PATH . DS . 'includes, que vous n\'avez pas modifier</small>';
                    }
                    die($contenu);
                    break;
                case '2A000':
                    if (__DEV_MODE) {
                        throw new Exception("Syntax Error: " . $e->getMessage(), $e->getCode());
                    }
                    break;
                default:
                    throw new Exception ($e->getMessage() .' ' . get_class ( $this ), $e->getCode());
                    break;
            }
        }
    }
}