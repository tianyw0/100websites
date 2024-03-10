<script setup>
import { reactive, onMounted, computed } from "vue"
import axios from "axios"

const text = reactive({
  plain: "",
  rich: "",
})
const risks = reactive({
  data: [],
})

const placeholder = reactive({
  hold: "",
  init: "请在左侧输入文本...",
  fixedAll: "正确无误！",
})

const state = reactive({
  inputing: true,
})

const hasData = computed(() => {
  return Object.keys(risks.data) && Object.keys(risks.data).length > 0
})

onMounted(() => {
  placeholder.hold = placeholder.init
  text.plain =
    "好的,习近总书记来指工作。我们要坚持四个意思。你说的非常有道。 立项很美好，现实很骨干"
})

function checkme() {
  if (!text.plain) {
    alert("内容不能为空")
  }
  axios
    // .post("http://47.94.16.211:8001/check_risk", {
    //   docText: text.plain,
    // })
    .post("http://60.205.190.227:8000/check_risk", {
      docText: text.plain,
    })
    // .post("http://222.128.103.228:18080/check_risk", {
    //   docText: text.plain,
    // })
    .then((response) => {
      risks.data = response.data.risks
      // console.log(risks.data);

      if (Object.keys(risks.data).length === 0) {
        // 显示“正确无误”
        placeholder.hold = placeholder.fixedAll
      } else {
        // 回显“标记的textarea”
        console.log("plain: ", text.plain)
        debugger
        let index = 0
        let waveSpanLeft = "<span class='u1 text-dark fw-normal'>"
        let spanRight = "</span>"
        let richText = ""
        // 需要排序
        risks.data.sort((a,b) => a.beginIndex - b.beginIndex)
        for (var i = 0; i < risks.data.length; i++) {
          let risk = risks.data[i]
          richText = richText.concat(text.plain.slice(index, risk.beginIndex))
          richText = richText.concat(
            waveSpanLeft +
              text.plain.slice(risk.beginIndex, risk.endIndex) +
              spanRight,
          )
          index = risk.endIndex
        }
        richText = richText.concat(text.plain.slice(index))
        text.rich = richText
        state.inputing = false
      }
    })
    .catch(function (error) {
      console.log(error)
    })
}
function onInput(event) {
  text.plain = event.target.innerText
}
</script>

<template>
  <!-- 顶部导航 -->
  <nav class="navbar navbar-expand-lg bg-dark navbar-dark fixed-top">
    <div class="container">
      <a href="#" class="navbar-brand fw-light">数据精灵·语法检测</a>

      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navmenu"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navmenu">
        <ul class="navbar-nav ms-auto">
          <!--margin start = margin left-->
          <li class="nav-item">
            <div class="nav-link">返回首页</div>
          </li>
          <li class="nav-item">
            <div class="nav-link">联系我们</div>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <!-- 中间内容 -->
  <section
    id="app"
    class="p-5 bg-light text-dark text-center text-sm-start"
    v-cloak
  >
    <div class="container">
      <div class="row">
        <div class="col-md-8">
          <div
            v-if="state.inputing"
            class="test_box border border-2 rounded-1 border-opacity-10 fw-light text-black-50"
            contenteditable="true"
            @input="onInput"
          >
            {{ text.plain }}
            <br />
          </div>
          <div
            v-else
            class="test_box border border-2 rounded-1 border-opacity-10 fw-light text-black-50 eee"
            contenteditable="true"
            v-html="text.rich"
          />
          <div class="my-4 d-md-flex justify-content-between">
            <p class="text-muted">全文字数100字 限制10000字符</p>
            <button class="btn btn-primary btn-sm" @click="checkme()">
              立即审核
            </button>
          </div>
        </div>
        <div class="col-md-4">
          <p class="display-6">检测结果</p>
          <div v-show="!hasData" class="alert alert-success" role="alert">
            {{ placeholder.hold }}
          </div>
          <ol
            v-show="hasData"
            class="list-group list-group-flush border border-2 rounded-1 border-opacity-10"
            style="max-height: 636px; overflow: auto"
          >
            <li
              v-for="item in risks.data"
              class="list-group-item d-flex justify-content-between align-items-start"
            >
              <div class="ms-2 me-auto">
                <!-- 四种不同的病句 类型，不同的显示 -->
                <div class="fw-bold" v-if="item.operation == 'REPLACE'">
                  <span class="text-danger">{{ item.source }}</span>
                  <i class="bi bi-arrow-right-short"></i>
                  {{ item.suggests[0] }}
                </div>
                <div class="fw-bold" v-if="item.operation == 'DELETE'">
                  <span class="text-danger text-decoration-line-through">{{
                    item.source
                  }}</span>
                </div>
                <div class="fw-bold" v-if="item.operation == 'APPEND'">
                  {{ item.suggests[0] }}
                </div>
                <div class="fw-bold" v-if="item.operation == 'MARK'">
                  <span class="text-danger">{{ item.source }}</span>
                </div>
                <!-- 显示句子上下文 -->
                <span class="text-muted text-sm-start fw-light">
                  {{ item.sentence }}
                </span>
              </div>
              <!-- 显示徽章 -->
              <span class="badge bg-primary rounded-pill">{{ item.type }}</span>
            </li>
          </ol>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped></style>