package spark_test_server;

import spark.Spark;

public class Main
{
    public static void main( String[] args )
    {
       Spark.get("/", (req, res) -> "Hello World");
    }
}