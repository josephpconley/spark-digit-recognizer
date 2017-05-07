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

  val five = new File("five-small.jpg")
  val fiveResized = new File("five-resized.jpg")

  Image.fromFile(five).resizeTo(28, 28).output(fiveResized)

  def convertToGrayScale(input: File, output: File) = {
    Image.fromFile(input).filter(GrayscaleFilter).output(output)
  }
}