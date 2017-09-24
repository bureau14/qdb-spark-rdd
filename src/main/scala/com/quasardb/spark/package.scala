package com.quasardb

import java.sql.Timestamp

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SQLContext}

import com.quasardb.spark.rdd._
import com.quasardb.spark.rdd.ts.{DoubleRDD, DoubleRDDFunctions, DoubleDataFrameFunctions, BlobRDD, BlobRDDFunctions, BlobDataFrameFunctions}

import net.quasardb.qdb.QdbTimeRangeCollection

package object spark {

  implicit class QdbContext(@transient val sqlContext : SQLContext) {
    def tagAsDataFrame(uri: String,
                       tag: String) = {
      new QdbTagRDD(sqlContext.sparkContext, uri, tag)
    }

    def qdbDoubleColumn(uri: String,
                        table: String,
                        column: String,
                        ranges: QdbTimeRangeCollection) = {
      new DoubleRDD(sqlContext.sparkContext, uri, table, column, ranges)
    }

    def qdbDoubleColumnAsDataFrame(uri: String,
                                   table: String,
                                   column: String,
                                   ranges: QdbTimeRangeCollection) = {
      qdbDoubleColumn(uri, table, column, ranges)
        .toDataFrame(sqlContext)
    }

    def qdbBlobColumn(uri: String,
                      table: String,
                      column: String,
                      ranges: QdbTimeRangeCollection) = {
      new BlobRDD(sqlContext.sparkContext, uri, table, column, ranges)
    }

    def qdbBlobColumnAsDataFrame(uri: String,
                                 table: String,
                                 column: String,
                                 ranges: QdbTimeRangeCollection) = {
      qdbBlobColumn(uri, table, column, ranges)
        .toDataFrame(sqlContext)
    }
  }

  implicit def toQdbDoubleRDDFunctions[A <: (Timestamp, Double)](rdd: RDD[A]): DoubleRDDFunctions[A] = {
    return new DoubleRDDFunctions[A](rdd)
  }

  implicit def toQdbDoubleDataFrameFunctions(data: DataFrame): DoubleDataFrameFunctions = {
    return new DoubleDataFrameFunctions(data)
  }

  implicit def toQdbBlobRDDFunctions[A <: (Timestamp, Array[Byte])](rdd: RDD[A]): BlobRDDFunctions[A] = {
    return new BlobRDDFunctions[A](rdd)
  }

  implicit def toQdbBlobDataFrameFunctions(data: DataFrame): BlobDataFrameFunctions = {
    return new BlobDataFrameFunctions(data)
  }
}
