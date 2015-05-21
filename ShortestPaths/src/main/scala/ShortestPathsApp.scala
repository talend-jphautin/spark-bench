
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src.main.scala
import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.{SparkContext,SparkConf, Logging}
import org.apache.spark.SparkContext._
import org.apache.spark.graphx._
import org.apache.spark.graphx.lib._
import org.apache.spark.graphx.util.GraphGenerators
import org.apache.spark.rdd._
import org.apache.spark.storage.StorageLevel


 object ShortestPathsApp {
 
    def main(args: Array[String]) {
    if (args.length < 3) {
	  println("usage: <input> <output> <minEdge> <numV> ")      
      System.exit(0)
    }
		Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)

    val conf = new SparkConf
    conf.setAppName("Spark ShortestPath Application")
    val sc = new SparkContext(conf)
    
	val input = args(0) 
    val output = args(1)
	val minEdge= args(2).toInt
	val numVertices= args(3).toInt
		
	val graph = GraphLoader.edgeListFile(sc, input, true, minEdge, StorageLevel.MEMORY_AND_DISK, StorageLevel.MEMORY_AND_DISK)	
	
	//val numVertices= graph.vertices.count()
	//val edges = sc.textFile(input).map { line =>
    //  val fields = line.split("::")
    //  Edge(fields(0).toLong , fields(1).toLong, fields(2).toDouble)
      
    //}
	//var numv=100;
	//if( numVertices <= 20000){
//		numv=numVertices/10
//	}else{
//		numv=500
//	}	
	val landmarks = Seq(1,numVertices ).map(_.toLong)
    val results=ShortestPaths.run(graph,landmarks).vertices
	
	results.saveAsTextFile(output)
	
    sc.stop();
    
  }
  
}