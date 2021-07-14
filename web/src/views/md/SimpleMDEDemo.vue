<template>
  <div>
    <div style="width:680px;display:inline;float:left" class="simplemde-container">
      <textarea id="demo"/>
    </div>
    <div
        class="simplemde-html"
        style="display: inline;float: left;margin-left: 40px;font-size: 18px;"
    ></div>
  </div>
</template>
<script lang="ts">
import {defineComponent, onMounted} from "vue";
import 'simplemde/dist/simplemde.min.css';
import SimpleMDE from "simplemde";

export default defineComponent({
  setup() {


    onMounted(() => {
      const simplemde = new SimpleMDE({
        element: document.getElementById("demo"),
        autofocus: true,
        toolbar: [
          "bold",
          "italic",
          "heading",
          "strikethrough",
          "quote",
          "heading-smaller",
          "heading-bigger",
          "code",
          "quote",
          "unordered-list",
          "ordered-list",
          "clean-block",
          "link",
          "image",
          "table",
          "horizontal-rule",
          "preview",
          "side-by-side",
          "fullscreen",
          "guide"
        ],
      });
      simplemde.isPreviewActive(); // returns boolean
      simplemde.isSideBySideActive();
    });


  }
});

</script>

<style scoped>
.simplemde-container >>> .CodeMirror {
  line-height: 20px;
}

.simplemde-container >>> .CodeMirror-scroll {

}

.simplemde-container >>> .CodeMirror-code {
  padding-bottom: 40px;
}

.simplemde-container >>> .editor-statusbar {
  display: none;
}

.simplemde-container >>> .CodeMirror .CodeMirror-code .cm-link {
  color: #1890ff;
}

.simplemde-container >>> .CodeMirror .CodeMirror-code .cm-string.cm-url {
  color: #2d3b4d;
}

.simplemde-container
>>> .CodeMirror
.CodeMirror-code
.cm-formatting-link-string.cm-url {
  padding: 0 2px;
  color: #e61e1e;
}

.simplemde-container >>> .editor-toolbar.fullscreen,
.simplemde-container >>> .CodeMirror-fullscreen {
  z-index: 1003;
}

.simplemde-html >>> * {
  margin-top: 4px;
  margin-bottom: 4px
}
</style>

