package com.viajobien.busy.models.routing

/**
 * @author david on 20/03/15.
 */
trait PathUtil {

  protected val PATH_PART_WILDCARD = "*"
  protected val PATH_WILDCARD = "**"

  /**
   * Precondition:
   * <pre>
   *   The path start with / and can end with or without /:
   *     Correct: /one/two/three
   *     Correct: /one/two/three/
   *     Incorrect: one/two/three
   *     Incorrect: one/two/three/
   * </pre>
   *
   * @param path
   * @param limit limit the split quantity, 0 for no limit
   * @return a list of splited path by /
   */
  def splitPath(path: String, limit: Int = 0, dropChars: Int = 1): List[String] = {
    val cleanedPath = this.cleanedPath(path)
    cleanedPath.substring(dropChars).split("/", limit).toList
  }

  /**
   * Remove query parameters
   * @param path
   * @return
   */
  private def cleanedPath(path: String): String = path.split('?')(0)

}
