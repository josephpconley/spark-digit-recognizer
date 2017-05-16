package img

import java.io.File

import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.filter.GrayscaleFilter

object ImageProcessor extends App {

  //4032 x 3024 image of a recent round of golf
//  val input = new File("golf.jpg")
//  val output = new File("golf-gray.jpg")
//
//  val outputImage = Image.fromFile(output)
//  val topLeft = Image(300, 300, outputImage.pixels(0, 0, 300, 300))
//
//  topLeft.output(new File("top-left.jpg"))

//  val five = new File("five-small.jpg")
//  val fiveResized = new File("five-resized.jpg")
//
//  Image.fromFile(five).resizeTo(28, 28).output(fiveResized)

//  val baneFront = new File("bane-front.jpg")
//  val img = Image.fromFile(baneFront)
//
//  val stepSize = img.width / 9
//  (0 until 9).map { idx =>
//    val score = Image(stepSize, img.height, img.pixels(idx * stepSize, 0, stepSize, img.height))
//    score.scaleToWidth(28).output(new File(s"target/scores/$idx.jpg"))
//  }

  val mnist = Image.fromFile(new File("images/mnist/5.png"))
  val golf = Image.fromFile(new File("images/five.jpg")).scaleToWidth(28)

  mnist.pixels.foreach(p => print(s"${(p.red + p.green + p.blue) / 3}-"))
  println
  golf.pixels.foreach(p => print(s"${255 - (p.red + p.green + p.blue) / 3}-"))

  def convertToGrayScale(input: File, output: File) = {
    Image.fromFile(input).filter(GrayscaleFilter).output(output)
  }
}