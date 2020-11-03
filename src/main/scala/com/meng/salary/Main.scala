package com.meng.salary

import java.io.{File, PrintWriter}

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val basePath = "C:\\Users\\admin\\salary"
    csv(basePath)
    printDetail(basePath)

  }
  def csv(basePath:String): Unit ={
    val basePath = "C:\\Users\\admin\\salary"
    val baseFile = new File(basePath)
    val files = baseFile.listFiles()
    for (f <- files; fileName = f.getName; if fileName.endsWith(".txt")) {
      println(fileName)
      val fName = fileName.split("\\.").head
      val so = Source.fromFile(f)("gbk")
      val list = so.getLines().toList
      val pattern = "^10\\|(.+)\\|(.+)\\|(.+)\\|\\|\\|\\|$".r.pattern
      val okLines = for (line <- list; matcher = pattern.matcher(line); if matcher.matches()) yield {
        val str = new String(line.replaceAll("\\|",",").getBytes,"utf-8")
        str
      }
      println(okLines.size)
      val writer = new PrintWriter(new File(basePath+s"\\$fName.csv"),"gbk")
      okLines.foreach(writer.println)
      writer.close()
    }
  }
  def printDetail(basePath:String): Unit = {
    val baseFile = new File(basePath)
    //    val baseFile = new File("D:\\360MoveData\\Users\\admin\\Documents\\WeChat Files\\wxid_xif4l5njq69421\\FileStorage\\File\\2020-07")
    val files = baseFile.listFiles()
    for (f <- files if f.getName.endsWith(".txt")) {
      val so = Source.fromFile(f)("gbk")
      val list = so.getLines().toList
      //      val pattern = "^10\\|([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)\\|(\\d{16,19})\\|([\\u4e00-\\u9fa5]{0,})\\|\\|\\|\\|$".r.pattern
      //      val pattern = "^10\\|([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)\\|(\\d{19,19})\\|([\\u4e00-\\u9fa5]{0,})\\|\\|\\|\\|$".r.pattern
      val pattern = "^10\\|(.+)\\|(.+)\\|(.+)\\|\\|\\|\\|$".r.pattern
      var maxSalary: Double = Double.MinValue
      var maxSalaryOne: String = ""
      var minSalary: Double = Double.MaxValue
      var minSalaryOne: String = ""
      val salaryList = for (l <- list; matcher = pattern.matcher(l); if matcher.matches()) yield {
        val salary = matcher.group(1).toDouble
        val name = matcher.group(3)
        //        println(name + ": " + salary)
        if (salary > maxSalary) {
          maxSalary = salary
          maxSalaryOne = name
        }
        if (salary < minSalary) {
          minSalary = salary
          minSalaryOne = name
        }
        salary
      }
      val allSalary = salaryList.sum
      val number = salaryList.length
      if (number > 0) {
        val avgSalary = allSalary / number
        println(f.getName + " ----> 总薪资: " + allSalary + ", 总人数: " + number + ", 平均薪资: " + avgSalary + ", 最高薪资: " + maxSalaryOne + " --> " + maxSalary + ", 最低薪资: " + minSalaryOne + " --> " + minSalary)
      } else {
        println("error!!! there is no match logs!")
      }
      so.close()
    }
  }
}
