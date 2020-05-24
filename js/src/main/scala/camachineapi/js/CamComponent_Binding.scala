//package camachineapi.js
//
//import camachineapi.CAMachine.Row
//import camachineapi.models._
//import com.thoughtworks.binding.Binding.{BindingSeq, Constants, Var}
//import com.thoughtworks.binding.{dom, Binding}
//import io.circe.parser._
//import io.circe.syntax._
//import org.scalajs.dom.ext.Ajax
//import org.scalajs.dom.html.TextArea
//import org.scalajs.dom.raw.Event
//import org.scalajs.dom.{document, Node}
//
//import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
//
//object CamComponent_Binding {
//
//  private lazy val protocolHost =
//    if (document.location.protocol.startsWith("file"))
//      "http://localhost:8080"
//    else
//      ""
//  val dataCam =
//    Var(
//      CamCompilerResult(
//        List(RecvarsDto("", "")),
//        List(VariableDto("", "")),
//        List(Row("", "", "")),
//        "",
//      ),
//    )
//
//  val answer = Var("")
//  private def requestCam(
//    opt: Int,
//    value: String,
//  ): Unit =
//    Ajax
//      .post(
//        s"$protocolHost/api/cam",
//        CamCompilerData(opt, value).asJson.toString(),
//      ).foreach(
//        xhr =>
//          parse(xhr.responseText)
//            .flatMap(_.as[CamCompilerResult])
//            .map(fillCam),
//      )
//
//  val dataLambda = Var(LambdaCode(""))
//  private def requestLambda(
//    value: String,
//  ): Unit =
//    Ajax
//      .post(s"$protocolHost/api/lambda", LambdaCode(value).asJson.toString()).foreach(
//        xhr =>
//          parse(xhr.responseText)
//            .flatMap(_.as[LambdaCode])
//            .map(fillLambda),
//      )
//
//  private def fillCam(
//    res: CamCompilerResult,
//  ): Unit =
//    dataCam.value = res
//
//  private def fillLambda(
//    res: LambdaCode,
//  ): Unit =
//    dataLambda.value = res
//
//  @dom def choice(
//    answer: Var[String],
//    question: String,
//    options: String*,
//  ): Binding[BindingSeq[Node]] = {
//    val id = System.currentTimeMillis
//    <p>{question}</p>
//      <ul>
//        {
//      for {
//        option <- Constants(options: _*)
//      } yield {
//        <li>
//            <input type="radio" name={s"$question/$id"} id={
//          s"$question/$option/$id"
//        } onclick={event: Event => answer := option}/>
//            <label htmlFor={s"$question/$option/$id"}>{option}</label>
//          </li>
//      }
//    }
//      </ul>
//  }
//
//  @dom def render = {
//
//    val someTextInput1: TextArea = {
//      <textarea id="command" name="command" placeholder="Последовательность команд через пробел" data:rows="10" data:cols="140" required="true"></textarea>
//    }
//    val someTextInput2: TextArea = {
//      <textarea id="command" name="command" placeholder="Последовательность лямбда-термов" data:rows="10" data:cols="140" required="true"></textarea>
//    }
//
//    <section>
//      <div class="wrapper">
//        <div class="jumbotron text-center" style="margin-bottom:0">
//          <h1>Categorical Abstract Machine</h1>
//        </div>
//      </div>
//    </section>
//    <div class="container" style="padding: 30px;">
//        <div class="container" style="margin-top:30px">
//          <div class="row">
//            <div class="col-sm-12">
//              <form>
//                <div class="form-group">
//
//                  <dl class=" " id="command_field">
//
//                    <dt>
//                      <label for="command">Код</label>
//                    </dt>
//
//                    <dd>
//                      {someTextInput1}
//                    </dd>
//
//
//                    <dd class="info">Required</dd>
//
//                  </dl>
//
//
//                </div>
//                <div class="form-group">
//                   {
//      choice(
//        answer,
//        "Оптимизация",
//        "Без оптимизации",
//        "Бета-свертывание",
//        "Двухместные функции",
//        "Бета-свертывание + Двухместные функции",
//      ).bind
//    }
//
//
//
//                </div>
//              </form>
//            </div>
//          </div> {
//      val opt = answer.bind match {
//        case "Без оптимизации"                        => 0
//        case "Бета-свертывание"                       => 1
//        case "Двухместные функции"                    => 2
//        case "Бета-свертывание + Двухместные функции" => 3
//        case _                                        => 0
//      }
//
//      <button class="btn btn-primary" onclick={
//        _: Event =>
//          requestCam(
//            opt,
//            someTextInput1.value,
//          )
//      }>Выполнить</button>
//    }
//          <div> {renderTable.bind}</div>
//          <div class="row">
//            <div class="col-sm-12">
//              <form>
//                <div class="form-group">
//
//                  <dl class=" " id="command_field2">
//
//                    <dt>
//                      <label for="command">Код</label>
//                    </dt>
//
//                    <dd>
//                      {someTextInput2}
//                    </dd>
//
//
//                    <dd class="info">Required</dd>
//
//                  </dl>
//
//
//                </div>
//              </form>
//            </div>
//          </div>
//          <button class="btn btn-primary" onclick={
//      _: Event =>
//        requestLambda(
//          someTextInput2.value,
//        )
//    }>Выполнить</button>
//          <ul>Терм ККЛ полученый из лямбда-термов</ul>
//
//          <table>
//            <tr>
//              <td style="bgcolor=#b9e4c9">{dataLambda.bind.code}</td>
//            </tr>
//          </table>
//
//        </div>
//      </div>
//      <footer>
//        <div class="wrapper">
//          <div class="jumbotron text-center" style="margin-bottom:0">
//            <p>НИЯУ МИФИ 2019</p>
//          </div>
//        </div>
//      </footer>
//  }
//  @dom def renderTable = {
//    val cam = dataCam.bind
//    <ul>Используемые переменные</ul>
//
//      <table>
//        {trRecvars(cam.recvars).bind}
//      </table>
//
//      <table>
//        {
//      trVariables(cam.variables).bind
//    }
//      </table>
//
//      <ul>Состояния КАМ на каждом шагу выполнения</ul>
//
//      <table>
//        {
//      trRows(cam.rows).bind
//    }
//      </table>
//
//    <ul>
//        {
//      cam.error
//    }
//      </ul>
//  }
//
//  @dom def trRecvars(recvars: List[RecvarsDto]) =
//    for {
//      recvar <- Constants(recvars: _*)
//    } yield {
//      <tr>
//        <td>{recvar.name}</td>
//        <td>{recvar.value}</td>
//      </tr>
//    }
//
//  @dom def trVariables(variables: List[VariableDto]) =
//    for {
//      variable <- Constants(variables: _*)
//    } yield {
//      <tr>
//        <td>{variable.name}</td>
//        <td>{variable.value}</td>
//      </tr>
//    }
//
//  @dom def trRows(rows: List[Row]) =
//    for {
//      row <- Constants(rows: _*)
//    } yield {
//      <tr>
//        <td style="bgcolor=#b9e4c9;">{row.term}</td>
//        <td style="bgcolor=#fedbd0;">{row.code}</td>
//        <td style="bgcolor=#b9e4c9;">{row.stack}</td>
//      </tr>
//    }
//}
