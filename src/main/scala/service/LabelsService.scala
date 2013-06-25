package service

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation

import model._
import Labels._

trait LabelsService {

  def getLabels(owner: String, repository: String): List[Label] =
    Query(Labels)
      .filter(l => (l.userName is owner.bind) && (l.repositoryName is repository.bind))
      .sortBy(_.labelName asc)
      .list

  def getLabel(owner: String, repository: String, labelId: Int): Option[Label] =
    Query(Labels)
      .filter(l => (l.userName is owner.bind) && (l.repositoryName is repository.bind) && (l.labelId is labelId.bind))
      .firstOption

  def createLabel(owner: String, repository: String, labelName: String, color: String): Unit =
    Labels.ins insert (owner, repository, labelName, color)

  def updateLabel(owner: String, repository: String, labelId: Int, labelName: String, color: String): Unit =
    Query(Labels)
      .filter { l => (l.userName is owner.bind) && (l.repositoryName is repository.bind) && (l.labelId is labelId.bind)}
      .map    { l => l.labelName ~ l.color }
      .update (labelName, color)

  def deleteLabel(owner: String, repository: String, labelId: Int): Unit = {
    // TODO delete ISSUE_LABEL
//    Query(Issues)
//      .filter { i => (i.userName is owner.bind) && (i.repositoryName is repository.bind) && (i.milestoneId is milestoneId.bind)}
//      .map    { i => i.milestoneId.? }
//      .update(None)

    Query(Labels)
      .filter { i => (i.userName is owner.bind) && (i.repositoryName is repository.bind) && (i.labelId is labelId.bind)}
      .delete
  }

}