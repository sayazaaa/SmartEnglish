<template>
  <el-container class="word-manager-container">
    <div class="page-content">
      <!-- 顶部操作栏 -->
      <div class="top-bar">
        <el-button type="primary" @click="openCreateDialog">新建单词</el-button>
        <el-button type="danger" @click="openDeleteDialog">删除</el-button>
      </div>

      <!-- 单词列表表格 -->
      <el-table
          :data="computedWords"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          border
      >
        <!-- 多选列 -->
        <el-table-column type="selection" width="55" />
        <!-- 显示单词本身 -->
        <el-table-column prop="word" label="单词" align="center" />
        <!-- 显示音标 -->
        <el-table-column prop="phonetic" label="音标" align="center" />
        <!-- 显示读音链接 -->
        <el-table-column
            prop="pronunciation"
            label="读音"
            align="center"
            width="120"
        >
          <template #default="scope">
            <a
                v-if="scope.row.pronunciation"
                :href="scope.row.pronunciation"
                target="_blank"
            >试听</a
            >
            <span v-else>—</span>
          </template>
        </el-table-column>
        <!-- 显示释义（只显示第一条）-->
        <el-table-column
            prop="explanations"
            label="释义示例"
            align="center"
        >
          <template #default="scope">
            <span>{{ (scope.row.explanations || [])[0] || "—" }}</span>
          </template>
        </el-table-column>
        <!-- 操作列：编辑/查看详情 -->
        <el-table-column label="操作" align="center" width="200">
          <template #default="scope">
            <el-button
                type="text"
                size="small"
                @click="openEditDialog(scope.row)"
            >
              编辑
            </el-button>
            <el-button
                type="text"
                size="small"
                @click="viewDetails(scope.row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 底部分页及统计：去掉 “10/page”，只保留 prev, pager, next, jumper -->
      <div class="footer-bar">
        <div class="total-info">共 {{ total }} 项数据</div>
        <el-pagination
            background
            layout="prev, pager, next, jumper"
            :total="total"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- ——— 新建单词对话框（Use POST /word） ——— -->
    <el-dialog
        title="新建单词"
        :visible.sync="createDialogVisible"
        width="600px"
        :before-close="() => (createDialogVisible = false)"
        append-to-body
    >
      <el-form
          ref="createFormRef"
          :model="newWord"
          :rules="wordRules"
          label-width="120px"
          label-position="left"
          class="dialog-form"
      >
        <!-- 单词 Spell -->
        <el-form-item label="单词" prop="word">
          <el-input v-model="newWord.word" placeholder="请输入单词"></el-input>
        </el-form-item>

        <!-- 音标 Phonetic -->
        <el-form-item label="音标" prop="phonetic">
          <el-input
              v-model="newWord.phonetic"
              placeholder="可选，示例：/ˈwɜːd/"
          />
        </el-form-item>

        <!-- 读音 Pronunciation -->
        <el-form-item label="读音 URL" prop="pronunciation">
          <el-input
              v-model="newWord.pronunciation"
              placeholder="可选，MP3 链接"
          />
        </el-form-item>

        <!-- 释义 Explanations: 多行文本，每行一个释义 -->
        <el-form-item label="释义(多行)" prop="explanations">
          <el-input
              type="textarea"
              v-model="newWord.explanationsText"
              placeholder="每行一个释义"
              rows="4"
          />
        </el-form-item>

        <!-- 同义词 Synonyms：分别输入 A/V/N Category，逗号分隔 -->
        <el-form-item label="同义词 A" prop="synA">
          <el-input
              v-model="newWord.synonyms.aText"
              placeholder="名词同义词，逗号分隔"
          />
        </el-form-item>
        <el-form-item label="同义词 V" prop="synV">
          <el-input
              v-model="newWord.synonyms.vText"
              placeholder="动词同义词，逗号分隔"
          />
        </el-form-item>
        <el-form-item label="同义词 N" prop="synN">
          <el-input
              v-model="newWord.synonyms.nText"
              placeholder="形容词等同义词，逗号分隔"
          />
        </el-form-item>

        <!-- 反义词 Antonyms：同理 -->
        <el-form-item label="反义词 A" prop="antA">
          <el-input
              v-model="newWord.antonyms.aText"
              placeholder="名词反义词，逗号分隔"
          />
        </el-form-item>
        <el-form-item label="反义词 V" prop="antV">
          <el-input
              v-model="newWord.antonyms.vText"
              placeholder="动词反义词，逗号分隔"
          />
        </el-form-item>
        <el-form-item label="反义词 N" prop="antN">
          <el-input
              v-model="newWord.antonyms.nText"
              placeholder="形容词等反义词，逗号分隔"
          />
        </el-form-item>

        <!-- 例句 Examples：多行，每行格式 “英文##中文##音频URL” -->
        <el-form-item label="例句 (每行 英文##中文##音频)" prop="examples">
          <el-input
              type="textarea"
              v-model="newWord.examplesText"
              placeholder="示例：I love you.##我爱你。##http://…mp3"
              rows="4"
          />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCreate">确认创建</el-button>
      </span>
    </el-dialog>

    <!-- ——— 编辑单词对话框（Use PUT /word） ——— -->
    <el-dialog
        title="编辑单词"
        :visible.sync="editDialogVisible"
        width="600px"
        :before-close="() => (editDialogVisible = false)"
        append-to-body
    >
      <el-form
          ref="editFormRef"
          :model="editWord"
          :rules="wordRules"
          label-width="120px"
          label-position="left"
          class="dialog-form"
      >
        <el-form-item label="单词" prop="word">
          <el-input v-model="editWord.word" disabled />
        </el-form-item>

        <el-form-item label="音标" prop="phonetic">
          <el-input v-model="editWord.phonetic" />
        </el-form-item>

        <el-form-item label="读音 URL" prop="pronunciation">
          <el-input v-model="editWord.pronunciation" />
        </el-form-item>

        <el-form-item label="释义(多行)" prop="explanations">
          <el-input
              type="textarea"
              v-model="editWord.explanationsText"
              rows="4"
          />
        </el-form-item>

        <el-form-item label="同义词 A" prop="synA">
          <el-input v-model="editWord.synonyms.aText" />
        </el-form-item>
        <el-form-item label="同义词 V" prop="synV">
          <el-input v-model="editWord.synonyms.vText" />
        </el-form-item>
        <el-form-item label="同义词 N" prop="synN">
          <el-input v-model="editWord.synonyms.nText" />
        </el-form-item>

        <el-form-item label="反义词 A" prop="antA">
          <el-input v-model="editWord.antonyms.aText" />
        </el-form-item>
        <el-form-item label="反义词 V" prop="antV">
          <el-input v-model="editWord.antonyms.vText" />
        </el-form-item>
        <el-form-item label="反义词 N" prop="antN">
          <el-input v-model="editWord.antonyms.nText" />
        </el-form-item>

        <el-form-item label="例句 (多行)" prop="examples">
          <el-input
              type="textarea"
              v-model="editWord.examplesText"
              rows="4"
          />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmEdit">保存修改</el-button>
      </span>
    </el-dialog>

    <!-- ——— 删除确认对话框 ——— -->
    <el-dialog
        title="提示"
        :visible.sync="deleteDialogVisible"
        width="350px"
        :before-close="() => (deleteDialogVisible = false)"
        append-to-body
    >
      <span>确定要删除所选单词吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmDelete">确认删除</el-button>
      </span>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";

/** 路由参数 wordsetId */
const route = useRoute();
const router = useRouter();
const wordsetId = route.params.wordsetId;

/** 分页参数 */
const currentPage = ref(1);
const pageSize = ref(10);
const words = ref([]); // 后端 GET /word 返回的数组
const total = computed(() => words.value.length);

/** 计算当前页要渲染的数据 */
const computedWords = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return words.value.slice(start, start + pageSize.value);
});

/** 表格选中行 */
const selectedWords = ref([]);

/** —— 新建单词 对话框 —— */
const createDialogVisible = ref(false);
const createFormRef = ref(null);
const newWord = reactive({
  word: "",
  phonetic: "",
  pronunciation: "",
  explanationsText: "", // 多行：每行一个释义
  synonyms: {
    aText: "",
    vText: "",
    nText: "",
  },
  antonyms: {
    aText: "",
    vText: "",
    nText: "",
  },
  examplesText: "", // 多行：每行 “英文##中文##音频URL”
});

/** —— 编辑单词 对话框 —— */
const editDialogVisible = ref(false);
const editFormRef = ref(null);
const editWord = reactive({
  id: null,
  word: "",
  phonetic: "",
  pronunciation: "",
  explanationsText: "",
  synonyms: {
    aText: "",
    vText: "",
    nText: "",
  },
  antonyms: {
    aText: "",
    vText: "",
    nText: "",
  },
  examplesText: "",
});

/** —— 删除确认 对话框 —— */
const deleteDialogVisible = ref(false);

/** 表单校验规则：大部分字段非空 */
const wordRules = {
  word: [{ required: true, message: "请输入单词", trigger: "blur" }],
  explanations: [
    {
      validator: (rule, value, callback) => {
        if (!newWord.explanationsText.trim()) {
          callback(new Error("请至少填写一条释义"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  // 其他可选字段无需必填，这里只对 word & explanationsText 做校验
};

/** 拉取单词列表 */
async function fetchWords() {
  try {
    // 假设后端支持：GET /word?wordsetId=<wordsetId>
    // 如果后端不需要 wordsetId，可删掉 params
    const resp = await axios.get("/word", {
      params: { wordsetId: wordsetId },
    });
    if (resp.status === 200) {
      // 后端返回格式假定为 Array<WordObject>
      words.value = Array.isArray(resp.data) ? resp.data : [];
    } else {
      ElMessage.error(`获取单词列表失败 (状态 ${resp.status})`);
    }
  } catch (err) {
    console.error("fetchWords 异常：", err);
    ElMessage.error("网络异常，无法获取单词列表");
  }
}

/** 分页页码变化 */
function handlePageChange(page) {
  currentPage.value = page;
}

/** 表格选中行变化 */
function handleSelectionChange(val) {
  selectedWords.value = val;
}

/** 点击“新建单词” */
function openCreateDialog() {
  // 重置表单数据
  newWord.word = "";
  newWord.phonetic = "";
  newWord.pronunciation = "";
  newWord.explanationsText = "";
  newWord.synonyms.aText = "";
  newWord.synonyms.vText = "";
  newWord.synonyms.nText = "";
  newWord.antonyms.aText = "";
  newWord.antonyms.vText = "";
  newWord.antonyms.nText = "";
  newWord.examplesText = "";
  createDialogVisible.value = true;
}

/** 点击“确认创建” */
async function confirmCreate() {
  createFormRef.value.validate(async (valid) => {
    if (!valid) return;

    try {
      // 处理字段：explanationsText → 数组
      const explanationsArr = newWord.explanationsText
          .split("\n")
          .map((line) => line.trim())
          .filter((line) => line !== "");

      // 同义词 & 反义词：逗号分隔 → 数组
      const synObj = {
        a: newWord.synonyms.aText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
        v: newWord.synonyms.vText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
        n: newWord.synonyms.nText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
      };
      const antObj = {
        a: newWord.antonyms.aText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
        v: newWord.antonyms.vText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
        n: newWord.antonyms.nText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
      };

      // 处理例句 examplesText：每行格式 “英文##中文##音频URL”
      const examplesArr = newWord.examplesText
          .split("\n")
          .map((line) => line.trim())
          .filter((line) => line !== "")
          .map((line) => {
            const parts = line.split("##").map((p) => p.trim());
            return {
              english: parts[0] || "",
              chinese: parts[1] || "",
              audio: parts[2] || "",
            };
          });

      // 最终提交 payload
      const payload = {
        word: newWord.word,
        phonetic: newWord.phonetic || null,
        pronunciation: newWord.pronunciation || null,
        explanations: explanationsArr,
        synonyms: synObj,
        antonyms: antObj,
        examples: examplesArr,
        wordsetId: wordsetId, // 如果后端需要关联词书ID
      };

      const resp = await axios.post("/word", payload);
      if (resp.status === 200) {
        ElMessage.success("单词创建成功");
        createDialogVisible.value = false;
        // 重置到第一页并刷新
        currentPage.value = 1;
        await fetchWords();
      } else {
        const msg = resp.data?.message || `创建失败 (状态 ${resp.status})`;
        ElMessage.error(msg);
      }
    } catch (err) {
      console.error("confirmCreate 异常：", err);
      ElMessage.error("网络异常，创建失败");
    }
  });
}

/** 点击“编辑”按钮：先拉该单词详情（GET /word?id=xxx） */
async function openEditDialog(row) {
  try {
    const resp = await axios.get("/word", { params: { id: row.id } });
    if (resp.status === 200) {
      const data = resp.data;
      // 填充到 editWord
      editWord.id = data.id;
      editWord.word = data.word;
      editWord.phonetic = data.phonetic || "";
      editWord.pronunciation = data.pronunciation || "";

      // 把 explanations 数组拼成多行文本
      editWord.explanationsText = (data.explanations || []).join("\n");

      // 同义词/反义词 → 拼成逗号分隔
      editWord.synonyms.aText = (data.synonyms?.a || []).join(",");
      editWord.synonyms.vText = (data.synonyms?.v || []).join(",");
      editWord.synonyms.nText = (data.synonyms?.n || []).join(",");

      editWord.antonyms.aText = (data.antonyms?.a || []).join(",");
      editWord.antonyms.vText = (data.antonyms?.v || []).join(",");
      editWord.antonyms.nText = (data.antonyms?.n || []).join(",");

      // 例句数组 → 拼成 “英文##中文##音频URL” 多行
      editWord.examplesText = (data.examples || [])
          .map(
              (ex) =>
                  `${ex.english || ""}##${ex.chinese || ""}##${ex.audio || ""}`
          )
          .join("\n");

      editDialogVisible.value = true;
    } else {
      ElMessage.error(`获取单词失败 (状态 ${resp.status})`);
    }
  } catch (err) {
    console.error("openEditDialog 异常：", err);
    ElMessage.error("网络异常，获取失败");
  }
}

/** 点击“保存修改” */
async function confirmEdit() {
  editFormRef.value.validate(async (valid) => {
    if (!valid) return;

    try {
      // 同上，处理各字段
      const explanationsArr = editWord.explanationsText
          .split("\n")
          .map((l) => l.trim())
          .filter((l) => l !== "");

      const synObj = {
        a: editWord.synonyms.aText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
        v: editWord.synonyms.vText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
        n: editWord.synonyms.nText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
      };

      const antObj = {
        a: editWord.antonyms.aText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
        v: editWord.antonyms.vText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
        n: editWord.antonyms.nText
            .split(",")
            .map((t) => t.trim())
            .filter((t) => t !== ""),
      };

      const examplesArr = editWord.examplesText
          .split("\n")
          .map((l) => l.trim())
          .filter((l) => l !== "")
          .map((l) => {
            const parts = l.split("##").map((p) => p.trim());
            return {
              english: parts[0] || "",
              chinese: parts[1] || "",
              audio: parts[2] || "",
            };
          });

      const payload = {
        id: editWord.id,
        word: editWord.word,
        phonetic: editWord.phonetic || null,
        pronunciation: editWord.pronunciation || null,
        explanations: explanationsArr,
        synonyms: synObj,
        antonyms: antObj,
        examples: examplesArr,
      };

      const resp = await axios.put("/word", payload);
      if (resp.status === 200) {
        ElMessage.success("单词更新成功");
        editDialogVisible.value = false;
        await fetchWords();
      } else {
        ElMessage.error(`更新失败 (状态 ${resp.status})`);
      }
    } catch (err) {
      console.error("confirmEdit 异常：", err);
      ElMessage.error("网络异常，更新失败");
    }
  });
}

/** 点击“删除”按钮 */
function openDeleteDialog() {
  if (selectedWords.value.length === 0) {
    ElMessage.warning("请先选择要删除的单词");
    return;
  }
  deleteDialogVisible.value = true;
}

/** 确认删除所选单词 */
async function confirmDelete() {
  try {
    const promises = selectedWords.value.map((row) =>
        axios.delete("/word", { params: { id: row.id } })
    );
    const results = await Promise.all(promises);
    const allOk = results.every((r) => r.status === 200);
    if (allOk) {
      ElMessage.success("删除成功");
      deleteDialogVisible.value = false;
      selectedWords.value = [];
      // 如果当前页条目刚好被删空，则翻到上一页
      if (
          computedWords.value.length === results.length &&
          currentPage.value > 1
      ) {
        currentPage.value -= 1;
      }
      await fetchWords();
    } else {
      ElMessage.error("部分删除失败，请重试");
    }
  } catch (err) {
    console.error("confirmDelete 异常：", err);
    ElMessage.error("网络异常，删除失败");
  }
}

/** 点击“查看详情”：这里只做一个示例，跳转到一个详情页或打开一个弹窗自行实现 */
function viewDetails(row) {
  // 举例：跳转到 /word-detail/:id
  router.push({ name: "WordDetail", params: { id: row.id } });
}

onMounted(() => {
  fetchWords();
});
</script>

<style scoped>
.word-manager-container {
  height: 100vh;
  background-color: #f9f6f2;
}
.page-content {
  padding: 20px;
  background-color: #faf6f2;
}

/* 顶部操作栏 */
.top-bar {
  margin-bottom: 16px;
}

/* 底部分页栏 */
.footer-bar {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total-info {
  color: #606266;
  font-size: 14px;
}

/* 对话框内表单样式 */
.dialog-form {
  max-height: 450px;
  overflow-y: auto;
}

/* 对话框底部按钮对齐 */
.dialog-footer {
  text-align: right;
}

/* el-dialog body 内部 padding 调整 */
.el-dialog__body {
  padding: 20px 30px;
}
</style>
