# DevPilot AI Learning Roadmap

这份文档用于记录 DevPilot AI 项目的学习路线、阶段目标、核心知识点和阶段复盘。后续每完成一个功能，都可以把设计原因、实现步骤、注意点和面试表达整理到这里。

## 学习方式

本项目采用“先理解，再设计，再实现，最后复盘”的节奏。

1. 先讲为什么：理解这个功能解决什么问题，为什么要这样拆分。
2. 再讲怎么设计：明确前端、后端、数据库、AI 模块分别负责什么。
3. 然后小步实现：一次只做一个清晰的小目标，保持代码可运行。
4. 最后做复盘：总结知识点、常见坑、优化方向和面试表达。

## 跨设备接力规则

如果在两台电脑上学习这个项目，需要把“对话线程同步”和“项目代码同步”分开理解。

对话线程同步依赖 Codex 或 ChatGPT 账号本身：两台电脑需要登录同一个账号，并且从历史列表中打开同一个线程。只有打开同一个线程，才是在继续同一段上下文；如果在另一台电脑中新建线程，即使账号相同，也只是一个新的对话。

项目代码同步依赖 GitHub：每完成一个小阶段，就提交代码并推送到远程仓库。另一台电脑通过 `git pull` 或重新 `git clone` 获取最新代码。GitHub 能保证代码和文档同步，但不会保存 Codex 对话内容。

为了尽量保证两台电脑的学习进度一致，建议遵守下面的顺序：

1. 当前电脑等 Codex 回答完成，不要在工具还在运行时切换设备。
2. 确认本地改动已经 `commit` 并 `push` 到 GitHub。
3. 在另一台电脑打开同一个 Codex 线程，确认最后一条消息能看到。
4. 在另一台电脑进入项目目录后执行 `git pull`，确认代码也是最新的。
5. 如果找不到同一个线程，就让新线程先阅读 `docs/learning-roadmap.md` 和当前代码，再继续学习。

注意点：

- 不建议两台电脑同时在同一个线程里发送不同任务，容易造成学习节奏混乱。
- 不能只依赖聊天记录保存项目进度，关键设计、阶段目标和代码讲解都应该沉淀到这份文档。
- 如果线程同步有延迟，先以 GitHub 上的最新提交和本文件记录为准。

## 项目定位

DevPilot AI 是一个 AI 知识库与项目助理系统，核心目标是练习 Java 后端、Vue 3 前端和 AI 应用工程能力。

用户可以上传项目文档、需求文档、接口文档或代码说明，系统会解析文档、切分文本、生成向量、保存到向量数据库。之后用户可以围绕知识库提问，系统通过 RAG 检索相关内容并生成回答。

## 技术栈

后端：

- Java 21
- Spring Boot 3
- Spring Web
- Spring Validation
- Spring Actuator
- 后续引入 Spring Data JPA、Spring Security、Spring AI
- PostgreSQL + pgvector
- Redis

前端：

- Vue 3
- TypeScript
- Vite
- Vue Router
- Pinia
- Axios
- 后续引入 Markdown 渲染、代码高亮、SSE 流式输出

工程化：

- Git + GitHub
- GitHub Actions
- Docker Compose

## 阶段 1：项目启动与工程结构

目标：把项目完整跑起来，并理解前后端为什么这样组织。

要完成的功能：

- 后端启动成功
- 前端启动成功
- 前端通过 `/api` 调用后端
- 理解 monorepo 目录结构
- 理解 GitHub 仓库与本地仓库关系

重点知识：

- monorepo 是什么
- Spring Boot 项目基本结构
- Vue 3 + Vite 项目基本结构
- 为什么后端常用 `8080`
- 为什么 Vite 默认常用 `5173`
- 为什么前端开发时需要 proxy
- 什么是 CORS

当前启动命令：

```bash
cd /Users/zhuzhendong/Documents/前端转全栈/devpilot-ai/backend
./mvnw spring-boot:run
```

另开一个 zsh 窗口：

```bash
cd /Users/zhuzhendong/Documents/前端转全栈/devpilot-ai/frontend
npm run dev
```

浏览器访问：

```text
http://localhost:5173
```

健康检查接口：

```text
http://localhost:8080/api/health
```

## 阶段 2：后端基础 API

目标：写出规范、可维护的 Java 后端接口。

要完成的功能：

- 知识库 CRUD
- 文档记录 CRUD
- 统一响应结构
- 统一异常处理
- 参数校验
- 数据库连接

重点知识：

- Controller / Service / Repository 分层
- DTO 和 Entity 的区别
- RESTful API 设计
- Bean Validation
- 统一异常处理
- 配置文件与环境变量
- 数据库迁移工具的作用

注意点：

- Controller 不写复杂业务逻辑。
- Service 负责业务流程编排。
- Repository 负责数据访问。
- 不直接把 Entity 暴露给前端，优先使用 DTO。

## 阶段 3：前端基础页面

目标：用 Vue 3 做出可维护的业务页面。

要完成的功能：

- 知识库列表页
- 知识库详情页
- 文档列表
- 文档上传入口
- 问答页面基础交互

重点知识：

- Composition API
- 单文件组件 SFC
- Pinia 状态管理
- Vue Router
- Axios 封装
- loading、empty、error 状态
- 组件拆分边界

注意点：

- 页面组件负责组织业务视图。
- 通用组件负责复用 UI。
- API 请求统一放在 `src/api`。
- 跨页面共享状态再放入 Pinia，不要什么都放 store。

## 阶段 4：知识库核心功能

目标：实现真正的知识库数据流。

要完成的功能：

- 文档上传
- 文档类型校验
- 文档解析
- 文本切分 chunk
- chunk 元数据保存
- 异步处理状态

重点知识：

- 文件上传协议
- MultipartFile
- PDF / Markdown / TXT 解析
- 文本切分策略
- chunk size 和 overlap
- 同步处理和异步处理的取舍
- 任务状态设计

注意点：

- 不要一开始就追求支持所有文件类型。
- 先支持 Markdown / TXT，再扩展 PDF。
- 文档处理可能耗时，后续应改成异步任务。
- chunk 必须保留来源信息，否则回答无法给引用。

## 阶段 5：AI / RAG 核心

目标：实现“基于文档内容回答问题”。

要完成的功能：

- 文本 embedding
- 向量入库
- 相似度检索
- Prompt 上下文拼接
- AI 回答
- 返回引用来源

重点知识：

- 什么是 embedding
- 什么是向量数据库
- 什么是相似度搜索
- 什么是 RAG
- 为什么不能把全部文档直接塞给模型
- Prompt 模板如何设计
- hallucination 是什么

注意点：

- 检索结果不是越多越好。
- Prompt 里要要求模型基于上下文回答。
- 回答要带 sources，方便用户判断可信度。
- embedding 模型和 chat 模型可以不同。

## 阶段 6：聊天体验

目标：做出接近真实 AI 产品的交互体验。

要完成的功能：

- 多轮会话
- 聊天历史
- SSE 流式输出
- Markdown 渲染
- 代码高亮
- 回答重新生成
- 知识库切换

重点知识：

- SSE 是什么
- SSE 和 WebSocket 的区别
- 前端如何消费流式响应
- 多轮上下文如何保存
- Markdown 安全渲染

注意点：

- 流式输出能明显提升用户体验。
- 聊天历史和知识库上下文是两件事。
- 不要无限制把历史消息塞进模型。

## 阶段 7：登录、权限与部署

目标：把项目打磨成作品集级别。

要完成的功能：

- 用户注册登录
- JWT 认证
- Spring Security
- 知识库权限控制
- 用量统计
- Docker Compose 一键启动
- GitHub Actions CI
- 部署说明

重点知识：

- 认证和授权的区别
- JWT 的结构
- Spring Security 过滤器链
- 多用户数据隔离
- Docker 网络
- CI 的价值

注意点：

- 权限逻辑必须在后端校验，不能只靠前端隐藏按钮。
- 密码不能明文保存。
- 生产环境不要把 API key 写进代码或提交到 GitHub。

## 阶段复盘模板

每完成一个阶段，用下面模板复盘。

```markdown
## 阶段 X 复盘

完成时间：

完成内容：

我学到了：

关键代码：

踩过的坑：

还能优化：

面试可以这样讲：
```

## 当前学习进度

- [x] 创建项目骨架
- [x] 推送到 GitHub
- [x] 跑通后端服务
- [x] 跑通前端服务
- [x] 理解前后端请求链路
- [x] 引入后端 Service 层
- [x] 引入后端 Repository 层
- [x] 实现创建知识库的前后端链路
- [x] 实现统一错误响应与前端错误提示
- [x] 接入 PostgreSQL、JPA 与 Flyway
- [x] 实现文档上传与文档列表
- [x] 实现文档文本切分 chunk
- [x] 实现关键词检索版问答来源返回
- [x] 为文档增加处理状态
- [x] 实现文档异步处理与前端轮询
- [x] 实现文档 chunk 预览

## 学习笔记 01：前后端请求链路

本节目标：理解用户在页面上点击一次提问按钮后，数据如何从 Vue 页面流向 Spring Boot 后端，再回到页面。

### 当前链路

```text
WorkspaceView.vue
  -> Pinia store: useKnowledgeStore
  -> API function: askKnowledgeBase
  -> Axios instance: http
  -> Vite dev server proxy: /api -> http://localhost:8080
  -> Spring Controller: KnowledgeController
  -> JSON response
  -> Pinia state update
  -> Vue template re-render
```

### 前端页面层

入口文件：

```text
frontend/src/views/WorkspaceView.vue
```

页面层负责：

- 展示输入框、按钮、回答区域。
- 接收用户输入的问题。
- 响应按钮点击事件。
- 从 store 读取 loading、answer、sources 等状态。

这里有一个核心方法：

```ts
function submitQuestion() {
  const trimmed = question.value.trim()
  if (!trimmed) return
  void store.ask(trimmed)
}
```

注意点：

- 页面不直接调用 axios，而是调用 store。
- 页面不关心接口地址，也不关心后端怎么实现。
- `trim()` 用于避免用户提交空白问题。
- `void store.ask(trimmed)` 表示触发异步动作，但当前函数不等待它返回。

### 前端状态层

入口文件：

```text
frontend/src/stores/knowledge.ts
```

Pinia store 负责管理知识库相关状态：

- `knowledgeBases`：知识库列表。
- `currentKnowledgeBaseId`：当前选中的知识库。
- `loading`：接口请求中状态。
- `latestAnswer`：最近一次回答。

提问时：

```ts
async function ask(question: string) {
  loading.value = true
  try {
    latestAnswer.value = await askKnowledgeBase(currentKnowledgeBaseId.value, question)
  } finally {
    loading.value = false
  }
}
```

注意点：

- `loading` 放在 store 中，页面就可以根据它禁用按钮。
- `try/finally` 确保请求成功或失败后都会关闭 loading。
- store 不直接写请求细节，而是调用 `src/api` 里的函数。

### 前端 API 层

入口文件：

```text
frontend/src/api/knowledge.ts
```

API 层负责把业务动作转换成 HTTP 请求：

```ts
export async function askKnowledgeBase(knowledgeBaseId: string, question: string) {
  const { data } = await http.post<AskKnowledgeResponse>(
    `/knowledge-bases/${knowledgeBaseId}/ask`,
    { question },
  )
  return data
}
```

注意点：

- API 函数名应该表达业务含义，而不是叫 `postData`。
- TypeScript interface 描述了前后端约定的数据结构。
- 这里的路径是 `/knowledge-bases/...`，不是完整的 `http://localhost:8080/...`，因为 `http` 已经配置了 baseURL。

### Axios 封装层

入口文件：

```text
frontend/src/api/http.ts
```

当前配置：

```ts
export const http = axios.create({
  baseURL: '/api',
  timeout: 30000,
})
```

这意味着：

- `http.get('/knowledge-bases')` 实际请求的是 `/api/knowledge-bases`。
- 超时时间是 30 秒，避免请求无限等待。

后续可以在这里统一加：

- JWT token
- 请求日志
- 错误处理
- 401 跳转登录

### Vite 代理层

入口文件：

```text
frontend/vite.config.ts
```

当前配置：

```ts
server: {
  port: 5173,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
  },
}
```

开发环境中的请求流向：

```text
浏览器 -> http://localhost:5173/api/knowledge-bases
Vite  -> http://localhost:8080/api/knowledge-bases
```

为什么需要代理：

- 前端开发服务器和后端服务器端口不同。
- 浏览器会把不同端口视为不同源。
- proxy 让前端代码像调用同源接口一样调用后端。
- 后续部署到生产环境时，通常由 Nginx 或网关承担类似职责。

### 后端 Controller 层

入口文件：

```text
backend/src/main/java/com/devpilot/ai/knowledge/KnowledgeController.java
```

当前 Controller：

```java
@RestController
@RequestMapping("/api/knowledge-bases")
public class KnowledgeController {
    @GetMapping
    public List<KnowledgeBaseSummary> listKnowledgeBases() {
        return List.of(
                new KnowledgeBaseSummary("demo", "Demo Knowledge Base", 0)
        );
    }
}
```

`@RestController` 的作用：

- 标记这是一个处理 HTTP 请求的类。
- 方法返回值会自动序列化成 JSON。

`@RequestMapping("/api/knowledge-bases")` 的作用：

- 给这个 Controller 下的所有接口统一加路径前缀。

`@GetMapping` 的作用：

- 表示这个方法处理 HTTP GET 请求。

当前后端还没有数据库，所以先返回写死的 demo 数据。这是合理的工程步骤，因为我们先验证前后端链路，再引入数据库复杂度。

### POST 提问接口

当前接口：

```java
@PostMapping("/{knowledgeBaseId}/ask")
public AskKnowledgeResponse ask(
        @PathVariable String knowledgeBaseId,
        @Valid @RequestBody AskKnowledgeRequest request
) {
    return new AskKnowledgeResponse(
            "This is a placeholder answer for knowledge base `%s`: %s"
                    .formatted(knowledgeBaseId, request.question()),
            List.of(new SourceReference("README.md", "Project overview", 0.92))
    );
}
```

这里有三个重要概念：

- `@PathVariable`：从 URL 路径中取变量，比如 `demo`。
- `@RequestBody`：从请求体 JSON 中读取数据。
- `@Valid`：触发参数校验。

请求示例：

```http
POST /api/knowledge-bases/demo/ask
Content-Type: application/json

{
  "question": "请总结这个知识库"
}
```

响应示例：

```json
{
  "answer": "This is a placeholder answer for knowledge base `demo`: 请总结这个知识库",
  "sources": [
    {
      "documentName": "README.md",
      "snippet": "Project overview",
      "score": 0.92
    }
  ]
}
```

### 为什么现在先写假数据

这不是偷懒，而是工程实践中的“垂直切片”。

垂直切片的意思是：先让一个最小功能从页面到后端完整跑通，再逐层替换内部实现。

当前切片：

```text
页面输入 -> 接口请求 -> 后端响应 -> 页面展示
```

后续我们会逐步替换：

- 写死知识库 -> 数据库知识库
- 写死回答 -> RAG 检索回答
- 写死来源 -> 真实文档 chunk 来源
- 普通响应 -> SSE 流式响应

好处：

- 每一步都能运行。
- 出问题时容易定位。
- 学习曲线更平滑。
- 更符合真实团队开发节奏。

### 本节你应该掌握

- 前端页面、store、api 三层分别负责什么。
- 为什么 axios 要封装。
- 为什么开发环境需要 Vite proxy。
- Spring Boot Controller 如何接收 GET / POST 请求。
- `@PathVariable`、`@RequestBody`、`@Valid` 的基本作用。
- 为什么先用 placeholder 数据跑通链路。

## 学习笔记 02：为什么要引入 Service 层

本节目标：把后端从“Controller 直接写业务逻辑”调整为“Controller 接收请求，Service 处理业务”。

### 改动前的问题

最初的 `KnowledgeController` 直接返回 demo 数据：

```java
@GetMapping
public List<KnowledgeBaseSummary> listKnowledgeBases() {
    return List.of(
            new KnowledgeBaseSummary("demo", "Demo Knowledge Base", 0)
    );
}
```

这在项目刚搭起来时可以接受，因为它帮助我们快速跑通前后端链路。但如果继续把所有逻辑都写在 Controller 里，后面会出现几个问题：

- Controller 会越来越胖。
- 数据库查询、文档解析、AI 调用、权限判断混在一起。
- 单元测试很难写。
- 同一段业务逻辑无法被多个接口复用。
- 前端接口层和后端业务层的边界会变模糊。

### Controller 应该负责什么

Controller 是 HTTP 入口层，主要职责是处理 Web 相关的事情：

- 定义 URL。
- 区分 GET / POST / PUT / DELETE。
- 读取路径参数、查询参数、请求体。
- 触发参数校验。
- 调用业务层。
- 返回响应对象。

Controller 不适合放复杂业务规则。

### Service 应该负责什么

Service 是业务层，主要职责是表达业务流程：

- 创建知识库。
- 查询知识库。
- 上传文档后的处理流程。
- 调用文档解析器。
- 调用向量检索。
- 拼接 Prompt。
- 调用 AI 模型。
- 返回业务结果。

后续真实的提问流程会长这样：

```text
KnowledgeController
  -> KnowledgeService
  -> DocumentChunkRepository
  -> VectorStore
  -> ChatModel
  -> AskKnowledgeResponse
```

所以现在先引入 Service 层，是为了给后续复杂业务留出位置。

### 本次代码结构

当前后端知识库模块：

```text
backend/src/main/java/com/devpilot/ai/knowledge/
  KnowledgeController.java
  KnowledgeService.java
  AskKnowledgeRequest.java
  AskKnowledgeResponse.java
  KnowledgeBaseSummary.java
  SourceReference.java
```

`KnowledgeController`：

```java
@RestController
@RequestMapping("/api/knowledge-bases")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }
}
```

`KnowledgeService`：

```java
@Service
public class KnowledgeService {
    public List<KnowledgeBaseSummary> listKnowledgeBases() {
        return List.of(
                new KnowledgeBaseSummary("demo", "Demo Knowledge Base", 0)
        );
    }
}
```

### Spring 是如何把 Service 传给 Controller 的

`KnowledgeService` 上有一个注解：

```java
@Service
```

它告诉 Spring：这个类是一个业务组件，请帮我创建对象并管理它。

Controller 构造函数里声明需要 `KnowledgeService`：

```java
public KnowledgeController(KnowledgeService knowledgeService) {
    this.knowledgeService = knowledgeService;
}
```

Spring 启动时会自动找到 `KnowledgeService`，创建实例，然后传给 `KnowledgeController`。这就是依赖注入。

### 为什么推荐构造函数注入

本项目使用构造函数注入，而不是字段注入：

```java
private final KnowledgeService knowledgeService;

public KnowledgeController(KnowledgeService knowledgeService) {
    this.knowledgeService = knowledgeService;
}
```

好处：

- 依赖关系一眼能看出来。
- 字段可以声明为 `final`，对象创建后不可变。
- 更容易写测试。
- 如果缺少依赖，应用启动时就会失败。

### 本节你应该掌握

- Controller 是 HTTP 入口层。
- Service 是业务层。
- `@Service` 表示让 Spring 管理业务组件。
- 构造函数注入是 Spring 项目里推荐的依赖注入方式。
- 这次改动不改变接口行为，只调整代码职责边界。

## 学习笔记 03：为什么要引入 Repository 层

本节目标：把“数据从哪里来”这件事从 Service 中拆出去，形成 `Controller -> Service -> Repository` 三层结构。

### 三层分别负责什么

当前后端知识库模块的调用链：

```text
KnowledgeController
  -> KnowledgeService
  -> KnowledgeRepository
  -> InMemoryKnowledgeRepository
```

三层职责：

- Controller：负责 HTTP 请求和响应。
- Service：负责业务流程和业务规则。
- Repository：负责数据访问。

这三个层次不要互相抢活。

### 为什么 Service 不应该直接写数据

改动前，Service 中直接写了 demo 数据：

```java
return List.of(
        new KnowledgeBaseSummary("demo", "Demo Knowledge Base", 0)
);
```

这会让 Service 同时承担两件事：

- 业务逻辑。
- 数据来源。

短期看没问题，但当数据来源从内存变成数据库、缓存、远程服务后，Service 会越来越难维护。

### Repository 的作用

Repository 层负责提供数据访问方法：

```java
public interface KnowledgeRepository {

    List<KnowledgeBase> findAll();

    Optional<KnowledgeBase> findById(String id);
}
```

注意这里定义的是接口，而不是直接把实现写死在 Service 里。

这样做的好处：

- Service 只依赖抽象，不关心数据来自哪里。
- 以后可以把内存实现替换成数据库实现。
- 更容易写单元测试。
- 数据访问逻辑集中管理。

### 为什么现在用 InMemoryRepository

当前实现：

```java
@Repository
public class InMemoryKnowledgeRepository implements KnowledgeRepository {

    private final Map<String, KnowledgeBase> knowledgeBases = Map.of(
            "demo", new KnowledgeBase("demo", "Demo Knowledge Base", 0)
    );
}
```

它的数据存在内存里，应用重启后不会持久化。

为什么还要这样做：

- 它比直接写在 Service 里更接近真实分层。
- 还不需要引入数据库复杂度。
- 可以保持接口行为不变。
- 后续替换成 JPA Repository 时，Service 改动会很小。

这叫“逐步演进”，不是一步到位。

### Entity / Domain / DTO 的区别

本次新增了：

```text
KnowledgeBase.java
KnowledgeBaseSummary.java
```

它们看起来字段一样，但角色不同。

`KnowledgeBase` 是后端内部的业务对象，表示知识库本身：

```java
public record KnowledgeBase(
        String id,
        String name,
        long documentCount
) {
}
```

`KnowledgeBaseSummary` 是返回给前端的 DTO：

```java
public record KnowledgeBaseSummary(
        String id,
        String name,
        long documentCount
) {
}
```

为什么不直接把内部对象返回给前端：

- 后端内部字段以后可能比前端需要的更多。
- 数据库字段可能包含敏感信息。
- API 返回结构应该稳定，不要轻易被内部模型影响。
- DTO 可以根据页面需要定制。

现在字段一样只是因为项目还很小，后面差异会越来越明显。

### Optional 的作用

Repository 中的查询方法：

```java
Optional<KnowledgeBase> findById(String id);
```

`Optional` 表示：这个结果可能存在，也可能不存在。

Service 中这样处理：

```java
KnowledgeBase knowledgeBase = knowledgeRepository.findById(knowledgeBaseId)
        .orElseThrow(() -> new KnowledgeBaseNotFoundException(knowledgeBaseId));
```

这比返回 `null` 更清晰，因为它强迫调用方处理“找不到”的情况。

### 为什么要有业务异常

新增异常：

```java
public class KnowledgeBaseNotFoundException extends RuntimeException {
    public KnowledgeBaseNotFoundException(String knowledgeBaseId) {
        super("Knowledge base not found: " + knowledgeBaseId);
    }
}
```

它表达的是一个业务问题：用户请求的知识库不存在。

不要让这种情况默默变成空结果，也不要让它变成模糊的服务器错误。

### 为什么要统一异常处理

新增：

```text
common/GlobalExceptionHandler.java
common/ApiErrorResponse.java
```

当 `KnowledgeBaseNotFoundException` 被抛出时，后端返回：

```http
404 Not Found
```

响应体类似：

```json
{
  "code": "KNOWLEDGE_BASE_NOT_FOUND",
  "message": "Knowledge base not found: xxx",
  "timestamp": "2026-07-03T09:00:00Z"
}
```

这样前端就能根据稳定的 `code` 做错误提示，而不是猜测后端错误文本。

### 本节你应该掌握

- Repository 是数据访问层。
- Service 不应该关心数据具体存在哪里。
- 先用内存 Repository，是为了逐步演进到数据库。
- DTO 和内部业务对象即使字段一样，也有不同职责。
- `Optional` 可以明确表达“可能没有结果”。
- 业务异常应该被映射成合适的 HTTP 状态码。

## 学习笔记 04：实现创建知识库

本节目标：实现一个完整的前后端写入链路，让用户可以在页面上创建新的知识库。

### 本节完成了什么

新增后端接口：

```http
POST /api/knowledge-bases
Content-Type: application/json

{
  "name": "My Project Docs"
}
```

成功响应：

```http
201 Created
Location: /api/knowledge-bases/{id}
```

响应体：

```json
{
  "id": "生成的 UUID",
  "name": "My Project Docs",
  "documentCount": 0
}
```

新增前端能力：

- 页面上输入知识库名称。
- 点击 `Create`。
- 前端调用创建接口。
- 创建成功后刷新知识库列表。
- 自动选中新创建的知识库。

### 后端：请求 DTO

新增文件：

```text
backend/src/main/java/com/devpilot/ai/knowledge/CreateKnowledgeBaseRequest.java
```

代码：

```java
public record CreateKnowledgeBaseRequest(
        @NotBlank(message = "Knowledge base name is required")
        @Size(max = 80, message = "Knowledge base name must be 80 characters or fewer")
        String name
) {
}
```

这个类是请求 DTO，专门描述“创建知识库”接口需要的请求体。

为什么不用 `KnowledgeBase` 直接接收请求：

- 创建时前端不应该传 `id`。
- 创建时 `documentCount` 应该由后端决定。
- 请求结构和内部业务对象不是一回事。
- DTO 可以承载参数校验规则。

### 后端：参数校验

本次用了两个校验注解：

```java
@NotBlank
@Size(max = 80)
```

含义：

- `@NotBlank`：不能是 `null`、空字符串或纯空格。
- `@Size(max = 80)`：名称最多 80 个字符。

Controller 中使用：

```java
@Valid @RequestBody CreateKnowledgeBaseRequest request
```

`@Valid` 会触发 DTO 上的校验规则。如果请求不合法，Spring 会返回 `400 Bad Request`。

注意：参数校验应该放在后端。前端也可以做校验，但前端校验只是用户体验，不能替代后端校验。

### 后端：RESTful 创建接口

新增接口：

```java
@PostMapping
public ResponseEntity<KnowledgeBaseSummary> createKnowledgeBase(
        @Valid @RequestBody CreateKnowledgeBaseRequest request
) {
    KnowledgeBaseSummary knowledgeBase = knowledgeService.createKnowledgeBase(request.name());
    URI location = URI.create("/api/knowledge-bases/" + knowledgeBase.id());
    return ResponseEntity.created(location).body(knowledgeBase);
}
```

这里有两个重点：

1. 创建资源用 `POST /api/knowledge-bases`。
2. 创建成功返回 `201 Created`，不是普通的 `200 OK`。

为什么返回 `Location`：

```http
Location: /api/knowledge-bases/{id}
```

它告诉客户端：新资源的位置在哪里。这是 RESTful API 中常见的设计。

### 后端：Service 负责创建业务

Service 中新增：

```java
public KnowledgeBaseSummary createKnowledgeBase(String name) {
    KnowledgeBase knowledgeBase = new KnowledgeBase(
            UUID.randomUUID().toString(),
            name.trim(),
            0
    );
    return toSummary(knowledgeRepository.save(knowledgeBase));
}
```

Service 负责的事情：

- 生成知识库 ID。
- 清理名称两侧空格。
- 设置初始文档数量为 0。
- 调用 Repository 保存。
- 把内部对象转换成返回给前端的 DTO。

为什么 ID 在后端生成：

- 前端不应该决定核心业务 ID。
- 后端可以统一保证 ID 规则。
- 未来换成数据库自增 ID、雪花 ID、UUID 都不影响前端。

### 后端：Repository 保存数据

Repository 接口新增：

```java
KnowledgeBase save(KnowledgeBase knowledgeBase);
```

内存实现：

```java
private final ConcurrentMap<String, KnowledgeBase> knowledgeBases = new ConcurrentHashMap<>();

@Override
public KnowledgeBase save(KnowledgeBase knowledgeBase) {
    knowledgeBases.put(knowledgeBase.id(), knowledgeBase);
    return knowledgeBase;
}
```

为什么不用普通 `HashMap`：

- Spring 应用通常是多线程处理请求。
- 多个用户可能同时创建知识库。
- `ConcurrentHashMap` 比普通 `HashMap` 更适合并发读写。

注意：这仍然不是持久化存储。应用重启后，内存数据会丢失。下一阶段接数据库就是为了解决这个问题。

### 前端：API 函数

新增：

```ts
export async function createKnowledgeBase(name: string) {
  const { data } = await http.post<KnowledgeBaseSummary>('/knowledge-bases', { name })
  return data
}
```

API 层负责：

- 定义请求方法。
- 定义请求路径。
- 定义请求体。
- 定义响应类型。

页面和 store 不应该手写 axios 细节。

### 前端：Pinia action

新增：

```ts
async function create(name: string) {
  creating.value = true
  try {
    const knowledgeBase = await createKnowledgeBase(name)
    await loadKnowledgeBases()
    currentKnowledgeBaseId.value = knowledgeBase.id
    latestAnswer.value = null
  } finally {
    creating.value = false
  }
}
```

这里有几个业务动作：

- 打开 `creating` 状态，让按钮显示创建中。
- 调用后端创建接口。
- 创建成功后重新加载知识库列表。
- 自动选中新创建的知识库。
- 清空旧回答，避免用户误以为旧回答属于新知识库。

### 前端：页面表单

页面新增：

```vue
<input
  v-model="newKnowledgeBaseName"
  class="text-input"
  placeholder="New knowledge base"
  @keyup.enter="submitKnowledgeBase"
/>
<button class="secondary-button" :disabled="store.creating" @click="submitKnowledgeBase">
  {{ store.creating ? 'Creating...' : 'Create' }}
</button>
```

这里的 `v-model` 做了双向绑定：

- 用户输入时，更新 `newKnowledgeBaseName`。
- 代码清空 `newKnowledgeBaseName` 时，输入框也会清空。

提交方法：

```ts
function submitKnowledgeBase() {
  const trimmed = newKnowledgeBaseName.value.trim()
  if (!trimmed) return
  void store.create(trimmed).then(() => {
    newKnowledgeBaseName.value = ''
  })
}
```

注意点：

- 前端先做空值判断，提升体验。
- 后端仍然要做 `@NotBlank`，保证安全。
- 创建成功后清空输入框。

### 完整请求链路

```text
用户输入名称
  -> WorkspaceView.vue submitKnowledgeBase
  -> Pinia store create
  -> API createKnowledgeBase
  -> POST /api/knowledge-bases
  -> KnowledgeController
  -> KnowledgeService
  -> KnowledgeRepository.save
  -> 返回 201 Created
  -> 前端刷新列表并选中新知识库
```

### 本节你应该掌握

- 创建资源通常使用 POST。
- 创建成功更合适的状态码是 `201 Created`。
- 请求 DTO 不等于内部业务对象。
- `@Valid` 触发后端参数校验。
- 前端校验负责体验，后端校验负责安全和正确性。
- Pinia action 可以表达一个完整的前端业务流程。
- 内存 Repository 可以练分层，但不能替代数据库。

## 学习笔记 05：统一错误响应与前端错误提示

本节目标：让后端错误返回稳定结构，让前端能把错误显示给用户。

### 为什么要做统一错误处理

真实项目里，请求不可能永远成功。常见错误包括：

- 用户提交空表单。
- 用户输入内容太长。
- 请求不存在的知识库。
- 用户未登录。
- 权限不足。
- 服务器内部异常。

如果每个接口都随便返回不同格式，前端就很难统一处理。

统一错误结构的目标是：

- 前端能稳定读取错误 code。
- 前端能稳定读取错误 message。
- 表单错误能定位到具体字段。
- 后端不用在每个 Controller 里重复写错误响应。

### 当前错误响应结构

当前后端错误响应：

```java
public record ApiErrorResponse(
        String code,
        String message,
        Instant timestamp,
        List<FieldErrorResponse> fieldErrors
) {
}
```

字段含义：

- `code`：机器可读的错误码，比如 `VALIDATION_ERROR`。
- `message`：人可读的错误摘要。
- `timestamp`：错误发生时间。
- `fieldErrors`：字段级错误列表。

字段错误：

```java
public record FieldErrorResponse(
        String field,
        String message
) {
}
```

例如创建知识库时传空名称，后端可以返回：

```json
{
  "code": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "timestamp": "2026-07-06T02:00:00Z",
  "fieldErrors": [
    {
      "field": "name",
      "message": "Knowledge base name is required"
    }
  ]
}
```

### 后端：处理参数校验错误

Spring 在 `@Valid` 校验失败时会抛出：

```java
MethodArgumentNotValidException
```

我们在 `GlobalExceptionHandler` 里统一处理：

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ApiErrorResponse> handleValidationError(MethodArgumentNotValidException exception) {
    List<FieldErrorResponse> fieldErrors = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> new FieldErrorResponse(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            ))
            .toList();

    ApiErrorResponse response = new ApiErrorResponse(
            "VALIDATION_ERROR",
            "Request validation failed",
            Instant.now(),
            fieldErrors
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}
```

这里返回的是：

```http
400 Bad Request
```

因为这是客户端传参不合法，不是服务器坏了。

### 后端：处理业务找不到错误

请求不存在的知识库时，我们返回：

```http
404 Not Found
```

对应代码：

```java
@ExceptionHandler(KnowledgeBaseNotFoundException.class)
public ResponseEntity<ApiErrorResponse> handleKnowledgeBaseNotFound(KnowledgeBaseNotFoundException exception) {
    ApiErrorResponse response = new ApiErrorResponse(
            "KNOWLEDGE_BASE_NOT_FOUND",
            exception.getMessage(),
            Instant.now(),
            List.of()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
}
```

`400` 和 `404` 的区别：

- `400`：请求格式或参数不合法。
- `404`：请求格式合法，但资源不存在。

### 前端：把后端错误转换成提示文案

前端在 `src/api/http.ts` 中新增：

```ts
export function getApiErrorMessage(error: unknown) {
  if (!axios.isAxiosError<ApiErrorResponse>(error)) {
    return 'Something went wrong.'
  }

  const response = error.response?.data
  const firstFieldError = response?.fieldErrors?.[0]

  if (firstFieldError?.message) {
    return firstFieldError.message
  }

  if (response?.message) {
    return response.message
  }

  return 'Request failed. Please try again.'
}
```

这个函数的作用是把复杂的 axios 错误对象转成一个简单字符串。

为什么要集中处理：

- 页面不需要理解 axios 错误结构。
- store 不需要重复写解析逻辑。
- 后续登录过期、权限不足等错误可以统一扩展。

### 前端：Store 保存错误状态

Pinia store 新增：

```ts
const errorMessage = ref<string | null>(null)
```

创建知识库时：

```ts
async function create(name: string) {
  creating.value = true
  errorMessage.value = null
  try {
    const knowledgeBase = await createKnowledgeBase(name)
    await loadKnowledgeBases()
    currentKnowledgeBaseId.value = knowledgeBase.id
    latestAnswer.value = null
    return true
  } catch (error) {
    errorMessage.value = getApiErrorMessage(error)
    return false
  } finally {
    creating.value = false
  }
}
```

这里有几个注意点：

- 请求前清空旧错误。
- 请求失败时保存错误文案。
- 返回 `true / false` 告诉页面操作是否成功。
- `finally` 中关闭 loading，避免按钮一直处于创建中。

### 前端：页面展示错误

页面中新增：

```vue
<p v-if="store.errorMessage" class="error-message">
  {{ store.errorMessage }}
</p>
```

这就是典型的状态驱动 UI：

```text
store.errorMessage 有值 -> 显示错误提示
store.errorMessage 为空 -> 不显示错误提示
```

### 本节你应该掌握

- 后端错误响应应该稳定、统一。
- `400 Bad Request` 适合参数校验错误。
- `404 Not Found` 适合资源不存在。
- `@RestControllerAdvice` 可以集中处理异常。
- 前端不应该直接展示原始异常对象。
- Pinia store 可以统一保存业务错误状态。
- 用户体验上，失败要有反馈，成功要清理旧错误。

## 学习笔记 06：接入 H2、PostgreSQL、JPA 与 Flyway

本节目标：把知识库从内存保存改成数据库保存，让数据在后端重启后仍然存在。当前默认使用本地 H2 文件数据库，PostgreSQL 作为后续可选 profile 保留。

### 为什么要接数据库

之前我们使用 `InMemoryKnowledgeRepository` 保存知识库。

它的优点是简单，适合早期练习分层；缺点也很明显：

- 后端重启后数据会丢失。
- 无法支持真实用户数据。
- 无法做复杂查询。
- 无法支持后续文档、chunk、会话等核心数据。

所以这一节开始引入持久化。考虑到本机还没有 Docker 和 PostgreSQL，默认开发环境先使用 H2 文件数据库，这样不安装额外软件也能继续学习。

### 本节引入了哪些东西

新增后端依赖：

- `spring-boot-starter-data-jpa`
- `flyway-core`
- `flyway-database-postgresql`
- `postgresql`
- `h2`

它们分别负责：

- JPA：用 Java 对象读写数据库。
- Flyway：管理数据库表结构版本。
- PostgreSQL Driver：后续让应用连接 PostgreSQL。
- H2：当前默认开发环境和测试环境使用的轻量数据库。

### 为什么不用 Hibernate 自动建表

配置中使用：

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

`validate` 的意思是：Hibernate 只校验实体和表结构是否匹配，不自动创建或修改表。

表结构由 Flyway 管理：

```text
backend/src/main/resources/db/migration/V1__create_knowledge_bases.sql
```

这样做的好处：

- 数据库变更有版本记录。
- 团队协作时每个人的表结构一致。
- 线上环境不会被 Hibernate 意外改表。
- 后续排查问题时能知道每次表结构怎么变的。

### Flyway 迁移脚本

当前脚本：

```sql
create table knowledge_bases (
    id varchar(36) primary key,
    name varchar(80) not null,
    document_count bigint not null default 0,
    created_at timestamp not null default current_timestamp
);

insert into knowledge_bases (id, name, document_count)
values ('demo', 'Demo Knowledge Base', 0);
```

命名规则：

```text
V1__create_knowledge_bases.sql
```

注意中间是两个下划线 `__`。

Flyway 会按照版本号顺序执行迁移：

```text
V1 -> V2 -> V3 ...
```

后续新增文档表时，我们会创建：

```text
V2__create_documents.sql
```

### Entity 是什么

新增：

```text
KnowledgeBaseEntity.java
```

它表示数据库中的一行数据：

```java
@Entity
@Table(name = "knowledge_bases")
public class KnowledgeBaseEntity {
    @Id
    @Column(length = 36)
    private String id;
}
```

几个关键注解：

- `@Entity`：告诉 JPA 这是数据库实体。
- `@Table`：指定对应的数据表。
- `@Id`：指定主键字段。
- `@Column`：指定列约束。

### 为什么 Entity 不是 record

之前的 `KnowledgeBase` 是 record：

```java
public record KnowledgeBase(String id, String name, long documentCount) {
}
```

但 JPA Entity 通常使用普通 class。

原因：

- JPA 需要无参构造函数。
- JPA 需要通过反射创建对象。
- Entity 往往需要和 ORM 生命周期配合。

所以当前有两种对象：

- `KnowledgeBaseEntity`：数据库实体。
- `KnowledgeBase`：业务层内部对象。

### Spring Data JPA Repository

新增：

```java
public interface JpaKnowledgeBaseRepository extends JpaRepository<KnowledgeBaseEntity, String> {

    List<KnowledgeBaseEntity> findAllByOrderByNameAsc();
}
```

`JpaRepository<KnowledgeBaseEntity, String>` 的含义：

- 管理的实体类型是 `KnowledgeBaseEntity`。
- 主键类型是 `String`。

Spring Data JPA 会自动提供：

- `findById`
- `findAll`
- `save`
- `deleteById`
- `count`

`findAllByOrderByNameAsc` 是按照 Spring Data 方法名约定生成查询：

```text
findAll      查询全部
OrderByName  按 name 排序
Asc          升序
```

### 为什么还保留自己的 KnowledgeRepository

当前结构：

```text
KnowledgeService
  -> KnowledgeRepository
  -> DatabaseKnowledgeRepository
  -> JpaKnowledgeBaseRepository
```

我们没有让 Service 直接依赖 `JpaKnowledgeBaseRepository`。

原因：

- Service 关心业务，不应该绑定 JPA 技术细节。
- 以后如果换 MyBatis、MongoDB、远程服务，Service 改动更小。
- 自定义 Repository 接口表达的是业务需要的数据访问能力。

这是依赖倒置思想的一个小实践。

### 当前默认为什么用 H2

主配置连接 H2 文件数据库：

```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/devpilot;MODE=PostgreSQL
```

这个数据库文件会生成在：

```text
backend/data/devpilot
```

好处：

- 不需要安装 Docker。
- 不需要安装 PostgreSQL。
- 后端重启后数据仍然存在。
- 适合当前阶段练习 JPA、Flyway、Repository 分层。

注意：H2 是开发学习阶段的轻量选择，不是最终生产数据库。后续做 pgvector、向量检索、部署时，我们仍然会切换到 PostgreSQL。

### PostgreSQL profile

PostgreSQL 配置被放在：

```text
backend/src/main/resources/application-postgres.yml
```

内容类似：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/devpilot
```

以后安装 Docker 或 PostgreSQL 后，可以这样启动：

```bash
cd /Users/zhuzhendong/Documents/前端转全栈/devpilot-ai
docker compose up -d postgres

cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

### 测试环境为什么也用 H2

测试配置：

```text
backend/src/test/resources/application.yml
```

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:devpilot-test;MODE=PostgreSQL
```

为什么测试用 H2：

- 不需要启动 Docker。
- 测试更快。
- CI 更容易跑。
- 适合当前阶段的基础验证。

注意：H2 不能完全替代 PostgreSQL。复杂 SQL、pgvector、全文检索等特性仍然要用 PostgreSQL 验证。

### 本地启动顺序

当前默认不需要 Docker，也不需要 PostgreSQL，直接启动后端：

```bash
cd /Users/zhuzhendong/Documents/前端转全栈/devpilot-ai
cd backend
./mvnw spring-boot:run
```

最后启动前端：

```bash
cd ../frontend
npm run dev
```

### 本节你应该掌握

- 数据库持久化解决内存数据重启丢失的问题。
- Flyway 负责数据库结构版本管理。
- JPA Entity 表示数据库表中的一行。
- Spring Data JPA Repository 可以自动生成常见 CRUD。
- Service 不直接依赖 JPA Repository，可以保持业务层更干净。
- H2 适合当前开发学习阶段，PostgreSQL 适合后续向量检索和部署阶段。

## 学习笔记 07：文档上传与文档列表

本节目标：打通知识库文档上传链路，把 `.txt/.md/.markdown` 文件保存到数据库，并在前端展示当前知识库的文档列表。

### 为什么先做文本文件

真实知识库会支持 PDF、Word、网页、代码仓库等多种来源。但一开始不应该直接做大而全。

我们先支持文本类文件：

- `.txt`
- `.md`
- `.markdown`

原因：

- 文本文件不需要复杂解析。
- 适合先打通上传、存储、列表展示链路。
- 后续做 chunk 切分和 embedding 时可以直接复用内容。
- PDF 解析会引入额外复杂度，应该放到基础链路稳定之后。

### 本节完成了什么

新增后端接口：

```http
GET /api/knowledge-bases/{knowledgeBaseId}/documents
```

用于查询当前知识库的文档列表。

新增后端接口：

```http
POST /api/knowledge-bases/{knowledgeBaseId}/documents
Content-Type: multipart/form-data
```

用于上传文档。

前端新增：

- 文件选择框。
- 上传按钮。
- 当前知识库文档列表。
- 上传成功后刷新文档列表。
- 上传成功后刷新知识库文档数量。

### 数据库表设计

新增 Flyway 脚本：

```text
backend/src/main/resources/db/migration/V2__create_knowledge_documents.sql
```

表结构：

```sql
create table knowledge_documents (
    id varchar(36) primary key,
    knowledge_base_id varchar(36) not null,
    filename varchar(255) not null,
    content_type varchar(120),
    size_bytes bigint not null,
    content text not null,
    created_at timestamp not null default current_timestamp,
    constraint fk_knowledge_documents_knowledge_base
        foreign key (knowledge_base_id)
        references knowledge_bases (id)
);
```

字段说明：

- `id`：文档 ID。
- `knowledge_base_id`：文档属于哪个知识库。
- `filename`：原始文件名。
- `content_type`：浏览器上传时提供的 MIME 类型。
- `size_bytes`：文件大小。
- `content`：文件文本内容。
- `created_at`：上传时间。

为什么要有外键：

```sql
foreign key (knowledge_base_id) references knowledge_bases (id)
```

它保证文档必须属于一个已经存在的知识库，避免出现“孤儿文档”。

### 后端对象分层

本节新增了三类对象。

业务对象：

```text
KnowledgeDocument.java
```

返回给前端的 DTO：

```text
KnowledgeDocumentSummary.java
```

数据库实体：

```text
KnowledgeDocumentEntity.java
```

这继续延续我们之前的原则：

```text
Entity 表示数据库结构
Domain 表示业务内部模型
DTO 表示接口输入输出
```

字段现在看起来很像，但角色不同，不要混用。

### MultipartFile 是什么

Controller 中上传接口：

```java
@PostMapping("/{knowledgeBaseId}/documents")
public ResponseEntity<KnowledgeDocumentSummary> uploadDocument(
        @PathVariable String knowledgeBaseId,
        @RequestParam("file") MultipartFile file
) {
    KnowledgeDocumentSummary document = knowledgeService.uploadDocument(knowledgeBaseId, file);
    URI location = URI.create("/api/knowledge-bases/%s/documents/%s".formatted(knowledgeBaseId, document.id()));
    return ResponseEntity.created(location).body(document);
}
```

`MultipartFile` 是 Spring 对上传文件的封装。

它可以读取：

- 文件名：`getOriginalFilename()`
- 文件大小：`getSize()`
- 文件类型：`getContentType()`
- 文件内容：`getBytes()`
- 是否为空：`isEmpty()`

上传文件时，前端请求不是 JSON，而是：

```http
Content-Type: multipart/form-data
```

### 后端文件校验

当前校验规则：

```java
if (file.isEmpty()) {
    throw new InvalidDocumentException("Document file is required.");
}

if (file.getSize() > 1024 * 1024) {
    throw new InvalidDocumentException("Document must be 1 MB or smaller.");
}

String filename = cleanFilename(file.getOriginalFilename()).toLowerCase();
if (!filename.endsWith(".txt") && !filename.endsWith(".md") && !filename.endsWith(".markdown")) {
    throw new InvalidDocumentException("Only .txt, .md, and .markdown files are supported for now.");
}
```

为什么要校验：

- 防止空文件。
- 防止超大文件拖垮服务。
- 当前阶段只支持文本文件。
- 后续不同文件类型要走不同解析器。

注意：前端的 `accept=".txt,.md,.markdown"` 只是用户体验，不能替代后端校验。用户可以绕过前端直接调用接口。

### 文件内容如何读取

当前实现：

```java
return new String(file.getBytes(), StandardCharsets.UTF_8);
```

这表示我们暂时把文件当成 UTF-8 文本读取。

注意点：

- 中文 Markdown 通常没问题。
- 如果用户上传非 UTF-8 文件，可能会乱码。
- 后续可以引入更完善的编码检测。
- PDF、Word 不能这样读取，需要专门解析器。

### 为什么上传时要加事务

Service 方法：

```java
@Transactional
public KnowledgeDocumentSummary uploadDocument(String knowledgeBaseId, MultipartFile file) {
    ...
    KnowledgeDocument savedDocument = documentRepository.save(document);
    knowledgeRepository.incrementDocumentCount(knowledgeBaseId);
    return toDocumentSummary(savedDocument);
}
```

这里做了两次数据库写入：

1. 保存文档。
2. 增加知识库的 `documentCount`。

它们应该一起成功或一起失败。

如果保存文档成功，但增加数量失败，就会出现数据不一致。所以这里使用 `@Transactional`。

### JPA 类型映射坑：text 和 Lob

本节一开始测试失败过一次：

```text
wrong column type encountered in column [content]
found [character varying], but expecting [clob]
```

原因是 Entity 上使用了 `@Lob`，Hibernate 期望数据库列是 `CLOB`，但 Flyway 脚本创建的是 `text`。

修复方式：

```java
@Column(nullable = false, columnDefinition = "text")
private String content;
```

这个例子说明：

- Flyway SQL 和 JPA Entity 必须保持一致。
- `ddl-auto: validate` 能帮我们尽早发现不一致。
- 这是关闭 Hibernate 自动建表的价值之一。

### 前端上传文件

API 函数：

```ts
export async function uploadKnowledgeDocument(knowledgeBaseId: string, file: File) {
  const formData = new FormData()
  formData.append('file', file)

  const { data } = await http.post<KnowledgeDocumentSummary>(
    `/knowledge-bases/${knowledgeBaseId}/documents`,
    formData,
  )
  return data
}
```

上传文件时要使用 `FormData`，不是普通 JSON。

浏览器会自动设置 multipart boundary，所以通常不要手动设置 `Content-Type`。

### 前端状态管理

Pinia store 新增：

- `documents`
- `uploading`
- `loadDocuments`
- `uploadDocument`

上传成功后：

```ts
await uploadKnowledgeDocument(currentKnowledgeBaseId.value, file)
await loadKnowledgeBases()
currentKnowledgeBaseId.value = knowledgeBaseId
await loadDocuments()
```

这里要注意一个小细节：刷新知识库列表可能会重置当前选中的知识库，所以需要先保存当前 ID，再恢复它。

### 本节你应该掌握

- 文件上传使用 `multipart/form-data`。
- Spring 用 `MultipartFile` 接收上传文件。
- 文件类型和大小必须在后端校验。
- 文档表应该通过外键关联知识库。
- 多次数据库写入需要考虑事务。
- `FormData` 是前端上传文件的常用方式。
- Flyway SQL 和 JPA Entity 不一致时，`ddl-auto: validate` 会帮你发现问题。

## 学习笔记 08：文档切分 Chunk

本节目标：在文档上传后，把原始文本切成多个小片段，并保存到 `document_chunks` 表，为后续 RAG 检索做准备。

### 为什么不能直接拿整篇文档问 AI

如果用户上传的是一篇很长的需求文档，直接把整篇文档塞给模型会有几个问题：

- 模型上下文窗口有限，长文档可能放不下。
- 即使放得下，也会浪费 token。
- 问一个小问题时，模型不需要看完整文档。
- 长上下文里相关信息容易被稀释。
- 后续做向量检索时，检索单位应该是片段，不是整篇文档。

所以 RAG 的常见做法是：

```text
文档
  -> 解析文本
  -> 切分 chunk
  -> 为每个 chunk 生成 embedding
  -> 用户提问时检索相关 chunk
  -> 把相关 chunk 塞进 Prompt
  -> 让模型回答
```

本节完成的是其中的：

```text
解析文本 -> 切分 chunk -> 保存 chunk
```

embedding 和向量检索放到后面。

### 数据库表设计

新增 Flyway 脚本：

```text
backend/src/main/resources/db/migration/V3__create_document_chunks.sql
```

表结构：

```sql
create table document_chunks (
    id varchar(36) primary key,
    knowledge_base_id varchar(36) not null,
    document_id varchar(36) not null,
    chunk_index integer not null,
    content text not null,
    char_start integer not null,
    char_end integer not null,
    created_at timestamp not null default current_timestamp
);
```

字段说明：

- `knowledge_base_id`：chunk 属于哪个知识库，方便以后按知识库检索。
- `document_id`：chunk 来自哪个文档，方便返回引用来源。
- `chunk_index`：chunk 在原文中的顺序。
- `content`：chunk 文本内容。
- `char_start` / `char_end`：chunk 在原文中的字符位置。

为什么同时保存 `knowledge_base_id` 和 `document_id`：

- 只保存 `document_id` 也能通过文档反查知识库。
- 但后续向量检索通常会按知识库过滤，直接保存 `knowledge_base_id` 查询更方便。

### TextChunker 的作用

新增：

```text
TextChunker.java
```

它专门负责文本切分：

```java
@Component
public class TextChunker {
    private static final int CHUNK_SIZE = 800;
    private static final int CHUNK_OVERLAP = 120;
}
```

为什么不直接把切分逻辑写在 `KnowledgeService` 里：

- Service 已经负责上传流程。
- 切分策略以后会变化。
- 独立组件更容易测试和替换。
- 后续可以从简单字符切分升级为 Markdown 标题切分、段落切分、token 切分。

### chunk size 是什么

当前：

```java
private static final int CHUNK_SIZE = 800;
```

意思是每个 chunk 最多大约 800 个字符。

为什么不是越大越好：

- 太大：检索结果不够精准。
- 太小：上下文不完整，语义容易断。

现在用 800 字符只是练习阶段的简单策略。后面接模型时，更合理的是按 token 估算。

### overlap 是什么

当前：

```java
private static final int CHUNK_OVERLAP = 120;
```

overlap 表示相邻 chunk 之间保留一段重复内容。

例如：

```text
chunk 1: 0    -> 800
chunk 2: 680  -> 1480
```

为什么要重叠：

- 重要信息可能刚好在边界处。
- 如果完全硬切，句子或段落会被切断。
- overlap 能降低边界丢信息的概率。

代价是：

- 存储会多一点。
- embedding 成本会多一点。
- 检索结果可能有重复。

这是 RAG 工程里非常常见的取舍。

### 上传后的新流程

现在上传文档的后端流程变成：

```text
KnowledgeService.uploadDocument
  -> 校验知识库存在
  -> 校验文件
  -> 读取文件内容
  -> 保存 knowledge_documents
  -> TextChunker.split
  -> 保存 document_chunks
  -> 更新知识库 documentCount
  -> 返回 KnowledgeDocumentSummary
```

代码片段：

```java
KnowledgeDocument savedDocument = documentRepository.save(document);
List<DocumentChunk> chunks = buildChunks(savedDocument);
chunkRepository.saveAll(chunks);
knowledgeRepository.incrementDocumentCount(knowledgeBaseId);
return toDocumentSummary(savedDocument);
```

这里仍然在 `@Transactional` 方法里。

原因是这三件事应该作为一个整体：

- 保存文档。
- 保存 chunk。
- 更新文档数量。

如果中间任何一步失败，都应该回滚。

### 为什么前端显示 chunk 数

`KnowledgeDocumentSummary` 新增：

```java
long chunkCount
```

前端文档列表现在显示：

```text
example.md
3 chunks
```

这有两个作用：

- 给用户反馈：系统已经处理了文档。
- 给开发者反馈：切分逻辑确实跑了。

后续接 embedding 时，我们还可以显示处理状态：

```text
Uploaded -> Chunked -> Embedded -> Ready
```

### 当前切分策略的局限

现在是按字符长度切分：

```java
int end = Math.min(start + CHUNK_SIZE, normalizedText.length());
String content = normalizedText.substring(start, end).trim();
```

它简单可靠，但不够聪明。

可能的问题：

- 会切断 Markdown 标题和正文。
- 会切断代码块。
- 会切断列表。
- 对中文、英文、代码的 token 长度没有区别处理。

后续可以优化为：

- 优先按 Markdown 标题切。
- 再按段落切。
- 再按 token 数切。
- 保留代码块完整性。

但工程上先用简单版本打通链路是合理的。

### 本节你应该掌握

- RAG 通常以 chunk 为检索单位，不直接检索整篇文档。
- chunk size 影响检索精准度和上下文完整度。
- overlap 用于减少边界信息丢失。
- chunk 要保存来源文档和原文位置，后续才能返回引用。
- 切分策略应该独立成组件，方便后续替换。
- 当前只是字符切分，后续会升级到更适合 Markdown/代码文档的切分策略。

## 学习笔记 09：关键词检索版问答

本节目标：先不接 AI，用简单关键词检索从 chunks 中找到相关片段，并把这些片段作为 sources 返回给前端。

### 为什么先做关键词检索

RAG 可以拆成两个核心步骤：

```text
Retrieval：检索相关上下文
Generation：基于上下文生成回答
```

很多初学者会急着先接 AI，但这会让问题变复杂：

- 不知道回答差是因为检索差，还是模型生成差。
- 不知道 Prompt 设计有没有问题。
- 不知道文档切分是否合理。
- 不知道 sources 是否真的来自知识库。

所以我们先做一个不用 AI 的版本：

```text
用户提问
  -> 从 document_chunks 中做关键词匹配
  -> 找出 top 5 chunk
  -> 返回临时回答和 sources
```

这一步练的是 RAG 里的 R，也就是 Retrieval。

### 当前 ask 流程

现在 `KnowledgeService.ask` 不再返回固定占位内容，而是：

```text
确认知识库存在
  -> retrieveRelevantChunks
  -> chunk 打分
  -> 取 top 5
  -> 转成 SourceReference
  -> 返回 AskKnowledgeResponse
```

如果没有命中：

```java
return new AskKnowledgeResponse(
        "当前知识库还没有检索到相关片段。你可以先上传 Markdown 或文本文件，再重新提问。",
        List.of()
);
```

如果命中：

```java
return new AskKnowledgeResponse(
        "我先用关键词检索找到了 %d 个相关片段。下一步接入 AI 后，会把这些片段作为上下文生成自然语言回答。",
        sources
);
```

注意：这还不是 AI 回答，只是检索结果说明。

### 检索 chunks

Repository 新增：

```java
List<DocumentChunk> findAllByKnowledgeBaseId(String knowledgeBaseId);
```

JPA 方法：

```java
List<DocumentChunkEntity> findAllByKnowledgeBaseIdOrderByCreatedAtDesc(String knowledgeBaseId);
```

这表示先把某个知识库下的所有 chunks 取出来，再在 Java 内存里做简单打分。

为什么现在可以这样做：

- 当前是练习阶段，数据量小。
- 逻辑简单，容易看懂。
- 后续会替换成向量数据库检索。

为什么以后不能一直这样做：

- chunk 数量变多后，全部加载到内存会慢。
- 关键词匹配不能理解语义。
- “登录流程”和“用户认证步骤”这种同义表达可能匹配不到。

### 提取搜索词

当前代码：

```java
private Set<String> extractSearchTerms(String question) {
    String normalizedQuestion = question.toLowerCase(Locale.ROOT).trim();
    Set<String> terms = new LinkedHashSet<>();

    for (String term : normalizedQuestion.split("[^\\p{IsAlphabetic}\\p{IsDigit}\\p{IsHan}]+")) {
        if (term.length() >= 2) {
            terms.add(term);
        }
    }

    for (int index = 0; index < normalizedQuestion.length() - 1; index++) {
        String term = normalizedQuestion.substring(index, index + 2).trim();
        if (term.codePoints().allMatch(Character::isLetter)) {
            terms.add(term);
        }
    }

    return terms;
}
```

这里做了两件事：

1. 按标点和空白拆出词。
2. 对中文问题额外生成相邻双字词。

为什么要额外处理中文：

英文通常有空格：

```text
login flow
```

中文通常没有空格：

```text
登录流程
```

如果只按空格拆，中文问题会变成一个很长的词，很难匹配。所以这里先用简单的双字切分，例如：

```text
登录流程 -> 登录、录流、流程
```

这不是专业中文分词，但足够作为练习阶段的关键词检索。

### chunk 打分

当前打分：

```java
private double scoreChunk(DocumentChunk chunk, Set<String> terms) {
    String content = chunk.content().toLowerCase(Locale.ROOT);
    double score = 0;

    for (String term : terms) {
        if (content.contains(term)) {
            score += Math.min(term.length(), 8);
        }
    }

    return score;
}
```

逻辑很直白：

- 如果 chunk 内容包含某个关键词，就加分。
- 词越长，分数越高。
- 单个词最多加 8 分，避免超长词影响过大。

局限：

- 只支持字面匹配。
- 不理解同义词。
- 不理解上下文。
- 不支持排序算法 BM25。
- 不支持向量语义搜索。

但它有一个优点：非常容易理解。

### 返回 sources

当前 `SourceReference` 包含：

```java
public record SourceReference(
        String documentName,
        String snippet,
        double score
) {
}
```

转换逻辑：

```java
private SourceReference toSourceReference(ScoredChunk scoredChunk) {
    DocumentChunk chunk = scoredChunk.chunk();
    String documentName = documentRepository.findById(chunk.documentId())
            .map(KnowledgeDocument::filename)
            .orElse("Unknown document");

    return new SourceReference(
            documentName,
            snippet(chunk.content()),
            scoredChunk.score()
    );
}
```

为什么要返回 sources：

- 用户可以知道答案依据来自哪个文档。
- 后续 AI 回答出现问题时，可以追溯检索结果。
- 面试时你可以说明自己考虑了可解释性。

### 为什么 snippet 要截断

当前：

```java
private String snippet(String content) {
    String singleLineContent = content.replace("\n", " ").trim();
    if (singleLineContent.length() <= 180) {
        return singleLineContent;
    }
    return singleLineContent.substring(0, 180) + "...";
}
```

原因：

- 前端 sources 区域不适合展示完整 chunk。
- snippet 只需要帮助用户判断来源是否相关。
- 完整 chunk 后续可以在详情页或引用弹窗中展示。

### 当前方案和真正向量检索的区别

关键词检索：

```text
问题词是否出现在 chunk 文本里
```

向量检索：

```text
问题语义和 chunk 语义是否相似
```

例子：

```text
问题：如何登录系统？
文档：用户认证流程包括账号密码校验和 token 签发。
```

关键词检索可能匹配不到，因为没有“登录”这个词。

向量检索更可能匹配到，因为它理解“登录”和“认证”语义接近。

所以这一节不是最终方案，而是为了让你先理解检索输入输出。

### 本节你应该掌握

- RAG 可以先拆成 Retrieval 和 Generation 两步。
- 先做关键词检索能帮助我们验证 chunk 和 sources 链路。
- 中文没有天然空格，简单关键词检索要额外处理。
- `score` 是我们对 chunk 相关性的粗略估计。
- sources 是 AI 知识库系统可信度的重要组成部分。
- 后续向量检索会替代当前关键词匹配。

## 代码阅读指南 01：后端分层怎么看

本节目标：帮助后端基础薄弱时更顺畅地阅读当前代码。建议你按下面顺序看，不要一上来就从数据库实体开始看。

### 第一步：先看 Controller

入口文件：

```text
backend/src/main/java/com/devpilot/ai/knowledge/KnowledgeController.java
```

Controller 是“HTTP 门面”，你可以把它理解成后端暴露给前端的门口。

它回答三个问题：

1. 这个接口的 URL 是什么？
2. 这个接口用 GET 还是 POST？
3. 收到请求后交给哪个 Service 方法？

例如：

```java
@GetMapping("/{knowledgeBaseId}/documents")
public List<KnowledgeDocumentSummary> listDocuments(@PathVariable String knowledgeBaseId) {
    return knowledgeService.listDocuments(knowledgeBaseId);
}
```

这段代码的意思是：

- 当前接口是 GET。
- 路径是 `/api/knowledge-bases/{knowledgeBaseId}/documents`。
- `{knowledgeBaseId}` 从 URL 中取出来。
- Controller 不自己查数据库，而是调用 `knowledgeService.listDocuments`。

为什么 Controller 不直接查数据库：

- Controller 应该只处理 HTTP 细节。
- 数据库逻辑属于 Repository。
- 业务规则属于 Service。
- 如果全写在 Controller，文件会很快变成大杂烩。

### 第二步：再看 Service

入口文件：

```text
backend/src/main/java/com/devpilot/ai/knowledge/KnowledgeService.java
```

Service 是“业务流程编排层”。当前最值得看的方法是：

```java
@Transactional
public KnowledgeDocumentSummary uploadDocument(String knowledgeBaseId, MultipartFile file) {
    ensureKnowledgeBaseExists(knowledgeBaseId);
    validateDocument(file);

    KnowledgeDocument document = new KnowledgeDocument(...);

    KnowledgeDocument savedDocument = documentRepository.save(document);
    knowledgeRepository.incrementDocumentCount(knowledgeBaseId);
    return toDocumentSummary(savedDocument);
}
```

这段代码可以按业务步骤读：

1. 确认知识库存在。
2. 校验文件是否为空、是否超大、类型是否支持。
3. 把上传文件转换成业务对象 `KnowledgeDocument`。
4. 保存文档。
5. 增加知识库文档数量。
6. 转成前端需要的 `KnowledgeDocumentSummary`。

这里的重点不是语法，而是顺序：Service 把一个业务动作拆成多个小步骤。

为什么这里有 `@Transactional`：

- 保存文档是一次数据库写入。
- 更新文档数量也是一次数据库写入。
- 如果其中一步失败，另一部也应该回滚。

所以事务保护的是“业务一致性”。

### 第三步：看 Repository 接口

入口文件：

```text
KnowledgeRepository.java
KnowledgeDocumentRepository.java
```

Repository 接口描述“业务层需要什么数据能力”。

例如：

```java
public interface KnowledgeDocumentRepository {

    List<KnowledgeDocument> findAllByKnowledgeBaseId(String knowledgeBaseId);

    KnowledgeDocument save(KnowledgeDocument document);
}
```

这不是 Spring Data JPA 的接口，而是我们自己定义的业务接口。

为什么要多这一层：

- Service 不直接依赖 JPA。
- 以后换数据库技术时，Service 尽量不改。
- 测试时可以替换成假的 Repository。

你可以把它理解成 Service 和数据库之间的“合同”。

### 第四步：看 Repository 实现

入口文件：

```text
DatabaseKnowledgeRepository.java
DatabaseKnowledgeDocumentRepository.java
```

这两个类负责把我们自己的业务 Repository 转接到 Spring Data JPA。

例如：

```java
public KnowledgeDocument save(KnowledgeDocument document) {
    KnowledgeDocumentEntity entity = new KnowledgeDocumentEntity(...);
    return toDomain(jpaRepository.save(entity));
}
```

它做了两件事：

1. 把业务对象 `KnowledgeDocument` 转成数据库实体 `KnowledgeDocumentEntity`。
2. 调用 JPA 的 `save` 保存到数据库。

为什么要转换：

- Domain 对象服务于业务。
- Entity 对象服务于数据库映射。
- DTO 对象服务于接口输入输出。

它们看起来字段类似，但变化原因不同，所以不要混成一个类。

### 第五步：最后看 Entity

入口文件：

```text
KnowledgeBaseEntity.java
KnowledgeDocumentEntity.java
```

Entity 是数据库表在 Java 里的映射。

例如：

```java
@Entity
@Table(name = "knowledge_documents")
public class KnowledgeDocumentEntity {
    @Id
    @Column(length = 36)
    private String id;
}
```

这段代码告诉 JPA：

- 这个类对应数据库表 `knowledge_documents`。
- `id` 是主键。
- `id` 对应的列长度是 36。

Entity 要和 Flyway SQL 对齐。

比如 SQL 中是：

```sql
content text not null
```

Entity 中就写：

```java
@Column(nullable = false, columnDefinition = "text")
private String content;
```

如果不对齐，`ddl-auto: validate` 会在启动时报错。这个报错虽然烦，但很有价值，因为它能提前发现表结构和 Java 代码不一致。

### 第六步：看全局异常处理

入口文件：

```text
backend/src/main/java/com/devpilot/ai/common/GlobalExceptionHandler.java
```

这里把后端异常转换成前端能稳定理解的 JSON。

例如：

```java
@ExceptionHandler(InvalidDocumentException.class)
public ResponseEntity<ApiErrorResponse> handleInvalidDocument(InvalidDocumentException exception) {
    ...
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}
```

这段代码的意思是：

- 如果 Service 抛出 `InvalidDocumentException`。
- 后端不要返回默认 500。
- 而是返回 `400 Bad Request`。
- 响应体使用统一的 `ApiErrorResponse`。

这样前端就可以统一显示错误提示。

### 当前后端请求链路总览

上传文档链路：

```text
浏览器选择文件
  -> POST multipart/form-data
  -> KnowledgeController.uploadDocument
  -> KnowledgeService.uploadDocument
  -> validateDocument
  -> readFileContent
  -> KnowledgeDocumentRepository.save
  -> JpaKnowledgeDocumentRepository.save
  -> knowledge_documents 表
```

查询文档链路：

```text
页面加载文档列表
  -> GET /documents
  -> KnowledgeController.listDocuments
  -> KnowledgeService.listDocuments
  -> KnowledgeDocumentRepository.findAllByKnowledgeBaseId
  -> JpaKnowledgeDocumentRepository.findAllByKnowledgeBaseIdOrderByCreatedAtDesc
  -> 返回 KnowledgeDocumentSummary[]
```

### 初学后端时最容易混淆的点

`@RestController`：
表示这是 HTTP 接口入口。

`@Service`：
表示这是业务逻辑组件。

`@Repository`：
表示这是数据访问组件。

`@Entity`：
表示这是数据库表映射。

`@Transactional`：
表示这个方法里的数据库操作要放在同一个事务里。

`@Valid`：
表示校验请求 DTO 上的规则。

`@RequestBody`：
表示从 JSON 请求体读取数据。

`@RequestParam("file")`：
表示从表单字段里读取上传文件。

### 推荐阅读顺序

每次看一个新功能，都按这个顺序：

```text
1. Controller：接口长什么样
2. Request/Response DTO：前后端传什么数据
3. Service：业务步骤是什么
4. Repository 接口：业务需要什么数据能力
5. Repository 实现：怎么落到数据库
6. Entity + migration：数据库表怎么设计
7. 前端 API + store + 页面：用户怎么操作
```

这样读代码会比从文件树里随机点开舒服很多。

## 学习笔记 10：文档处理状态

本节目标：给上传后的文档增加处理状态，让系统能表达“处理中、已完成、失败”这些业务状态。

### 为什么需要处理状态

真实的知识库系统里，上传文件并不等于文件马上可用。

一次完整的文档处理通常包括：

```text
上传文件
  -> 保存原始文档
  -> 解析文本
  -> 文本切分 chunk
  -> 生成 embedding
  -> 写入向量数据库
  -> 标记为可检索
```

这些步骤可能耗时，也可能失败。如果数据库里只有“文档名称、内容、上传时间”，前端就不知道文档到底能不能参与问答。

所以我们给文档增加状态：

```java
public enum DocumentProcessingStatus {
    PROCESSING,
    READY,
    FAILED
}
```

状态含义：

- `PROCESSING`：文档已经保存，正在处理。
- `READY`：文档处理完成，可以参与检索。
- `FAILED`：文档处理失败，后续可以展示错误或提供重试。

### 为什么用 enum，不直接用 String

如果直接写字符串：

```java
"READY"
```

坏处是容易写错，比如：

```java
"REDAY"
```

这种错误编译器发现不了，运行后才可能暴露。

使用 `enum` 后，状态值只能从固定集合中选择：

```java
DocumentProcessingStatus.READY
```

这样更适合表达有限状态。

### 数据库如何保存 enum

Entity 中使用：

```java
@Enumerated(EnumType.STRING)
@Column(nullable = false, length = 32)
private DocumentProcessingStatus processingStatus;
```

这里最重要的是 `EnumType.STRING`。

它会把枚举名保存成数据库字符串：

```text
READY
PROCESSING
FAILED
```

不要轻易使用 `EnumType.ORDINAL`，因为它保存的是枚举顺序数字。以后如果调整 enum 顺序，旧数据含义就可能错乱。

### Flyway 迁移脚本

本次新增了迁移：

```text
backend/src/main/resources/db/migration/V4__add_document_processing_status.sql
```

核心 SQL：

```sql
alter table knowledge_documents
    add column processing_status varchar(32) not null default 'READY';

alter table knowledge_documents
    add column processing_error varchar(500);
```

为什么 `processing_status` 要有默认值？

因为本地数据库可能已经有旧文档。旧文档创建时还没有这个字段，如果新增 `not null` 字段却不给默认值，数据库不知道旧数据该填什么，迁移就可能失败。

默认成 `READY` 的含义是：历史文档在之前已经完成了文本保存和 chunk 切分，所以可以认为它们已经可检索。

### 当前上传流程

现在的 `KnowledgeService.uploadDocument` 流程是：

```text
校验知识库是否存在
  -> 校验文件类型和大小
  -> 读取文本内容
  -> 保存 PROCESSING 文档
  -> 切分 chunk
  -> 保存 chunks
  -> 更新文档为 READY
  -> 知识库 documentCount + 1
```

对应代码：

```java
KnowledgeDocument savedDocument = documentRepository.save(document);
List<DocumentChunk> chunks = buildChunks(savedDocument);
chunkRepository.saveAll(chunks);
KnowledgeDocument readyDocument = documentRepository.save(markDocumentReady(savedDocument));
```

当前还是同步处理，所以用户上传完成后通常直接看到 `READY`。但后续如果改成异步任务，`PROCESSING` 就会短暂展示在页面上。

### 前端为什么也要改类型

后端返回 DTO 增加字段后，前端接口类型也要同步：

```ts
export interface KnowledgeDocumentSummary {
  id: string
  filename: string
  contentType: string | null
  sizeBytes: number
  processingStatus: 'PROCESSING' | 'READY' | 'FAILED'
  processingError: string | null
  createdAt: string
  chunkCount: number
}
```

TypeScript 中这段：

```ts
processingStatus: 'PROCESSING' | 'READY' | 'FAILED'
```

表示状态只能是三个字符串之一。它和后端的 Java enum 是一一对应的。

### 本节你需要掌握的后端知识

- 业务状态应该显式建模，不要只靠“有没有数据”来猜。
- Java enum 适合表达有限状态。
- Entity 改字段时，Flyway migration 也要同步。
- 给旧数据新增 `not null` 字段时，要考虑默认值。
- DTO 是前后端契约，后端改 DTO，前端类型也要跟着改。
- 当前同步处理简单直观，后续异步处理更适合真实文件解析和 embedding 场景。

## 学习笔记 11：异步文档处理

本节目标：把文档切分从上传请求中拆出来，交给后台线程处理，让上传接口更接近真实项目的设计。

### 为什么要异步处理

上一节我们已经有了文档状态：

```text
PROCESSING -> READY / FAILED
```

但当时文档切分仍然发生在上传接口里面：

```text
前端上传文件
  -> 后端保存文档
  -> 后端切分 chunk
  -> 后端保存 chunk
  -> 后端返回响应
```

这个流程的问题是：如果后续加入 PDF 解析、embedding、向量入库，上传接口会越来越慢。用户可能只是上传一个文件，却要等几十秒才能收到响应。

更真实的做法是：

```text
前端上传文件
  -> 后端保存原始文档，状态为 PROCESSING
  -> 后端立即返回响应
  -> 后台线程继续切分 chunk
  -> 成功后标记 READY
  -> 失败后标记 FAILED
```

这样用户能更快看到“文件已接收”，后端耗时任务也能独立处理。

### 本次后端拆分

新增了异步配置：

```text
backend/src/main/java/com/devpilot/ai/config/AsyncConfig.java
```

核心代码：

```java
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor documentProcessingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("document-processing-");
        executor.initialize();
        return executor;
    }
}
```

`@EnableAsync` 的作用是开启 Spring 的异步方法能力。

`ThreadPoolTaskExecutor` 的作用是创建一个线程池。线程池可以理解成“后台工人队伍”：

- `corePoolSize`：平时保留几个工人。
- `maxPoolSize`：忙的时候最多几个工人。
- `queueCapacity`：工人忙不过来时，队列里最多排多少任务。
- `threadNamePrefix`：给线程起名字，方便以后看日志排查问题。

### 为什么要单独创建 DocumentProcessingService

新增了：

```text
backend/src/main/java/com/devpilot/ai/knowledge/DocumentProcessingService.java
```

里面的核心方法是：

```java
@Async("documentProcessingExecutor")
@Transactional
public void processDocument(String documentId) {
    ...
}
```

为什么不直接把 `@Async` 写在 `KnowledgeService` 的私有方法上？

因为 Spring 的 `@Async` 依赖代理机制。简单理解：外部调用 Spring Bean 的 public 方法时，Spring 可以在中间“拦一下”，把这个方法丢到线程池里执行。

如果同一个类内部自己调用自己的方法，通常不会经过这个代理，`@Async` 就可能不生效。

所以我们把后台处理拆成独立 Service：

```text
KnowledgeService
  -> 调用 DocumentProcessingService.processDocument
```

这样调用会经过 Spring 代理，异步才更可靠。

### 为什么后台线程只传 documentId

代码里没有把整个 `KnowledgeDocument` 对象直接传给后台线程，而是传：

```java
documentProcessingService.processDocument(documentId);
```

后台线程再重新查数据库：

```java
documentRepository.findById(documentId)
```

原因是：JPA Entity 和数据库会话有关，不适合跨线程传递。跨线程传 `id` 更稳定，也更像真实系统里的任务消息。

以后如果接入消息队列，也通常是传：

```json
{
  "documentId": "..."
}
```

而不是传整个 Entity。

### 为什么要等事务提交后再启动后台线程

上传方法仍然有事务：

```java
@Transactional
public KnowledgeDocumentSummary uploadDocument(...) {
    ...
}
```

如果在事务还没提交时就启动后台线程，后台线程可能立刻去数据库查文档，但这条文档记录还没真正提交，查不到。

所以本次使用：

```java
TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
    @Override
    public void afterCommit() {
        documentProcessingService.processDocument(documentId);
    }
});
```

意思是：等当前事务提交成功之后，再启动后台处理。

这类细节非常后端。很多异步任务的 bug 都来自“任务跑得太快，数据还没提交”。

### 当前上传链路

现在上传文档变成：

```text
POST /documents
  -> KnowledgeController.uploadDocument
  -> KnowledgeService.uploadDocument
  -> 保存 PROCESSING 文档
  -> documentCount + 1
  -> 当前事务提交
  -> afterCommit 触发后台处理
  -> DocumentProcessingService.processDocument
  -> 切分 chunk
  -> 保存 chunks
  -> 更新文档为 READY
```

如果切分失败：

```text
DocumentProcessingService.processDocument
  -> catch RuntimeException
  -> 更新文档为 FAILED
  -> 保存 processingError
```

### 前端为什么需要轮询

上传接口现在可能很快返回：

```json
{
  "processingStatus": "PROCESSING"
}
```

但后端处理完成是在稍后发生的。前端如果不刷新，就会一直停留在 `PROCESSING`。

所以 store 里增加了轻量轮询：

```ts
function scheduleProcessingPollIfNeeded() {
  if (!documents.value.some((document) => document.processingStatus === 'PROCESSING')) {
    return
  }

  if (processingPollTimer !== null) {
    return
  }

  processingPollTimer = window.setTimeout(async () => {
    processingPollTimer = null
    await loadDocuments()
  }, 1500)
}
```

这段逻辑的意思是：

- 如果没有处理中状态，就不轮询。
- 如果已经有一个定时器，就不重复创建。
- 1.5 秒后重新加载文档列表。
- 加载后如果仍然有 `PROCESSING`，再安排下一次轮询。

后续更高级的方式可以用 WebSocket 或 SSE，但当前阶段轮询更简单，也更容易理解。

### 本节你需要掌握的后端知识

- 耗时任务不应该长期阻塞 HTTP 请求。
- `@Async` 可以把任务交给后台线程。
- 异步能力通常要配线程池，不要完全依赖默认配置。
- `@Async` 要通过 Spring Bean 代理调用才可靠。
- 后台任务最好传 ID，再自己查数据库。
- 异步任务要注意事务提交时机。
- 前端面对后台任务时，通常需要轮询、SSE 或 WebSocket 来刷新状态。

## 学习笔记 12：文档 Chunk 预览

本节目标：新增一个接口和页面区域，让我们能查看某篇文档被切成了哪些 chunks。

### 为什么要做 chunk 预览

RAG 的核心不是“把整篇文档直接塞给模型”，而是：

```text
用户提问
  -> 检索相关 chunks
  -> 把相关 chunks 拼进 Prompt
  -> 调用模型生成回答
```

所以 chunk 的质量会直接影响最终回答。

如果切分结果不可见，我们就很难判断：

- chunk 是否太长。
- chunk 是否太短。
- 关键信息是否被切断。
- overlap 是否保留了足够上下文。
- 后续检索结果为什么命中或没命中。

做 chunk 预览，就是给 RAG 系统加一个调试窗口。

### 后端新增接口

新增接口：

```text
GET /api/knowledge-bases/{knowledgeBaseId}/documents/{documentId}/chunks
```

这个路径表示：

- chunks 属于某个 document。
- document 又属于某个 knowledge base。
- 所以这是一个嵌套资源查询。

Controller 代码：

```java
@GetMapping("/{knowledgeBaseId}/documents/{documentId}/chunks")
public List<DocumentChunkSummary> listDocumentChunks(
        @PathVariable String knowledgeBaseId,
        @PathVariable String documentId
) {
    return knowledgeService.listDocumentChunks(knowledgeBaseId, documentId);
}
```

Controller 只负责接 HTTP 参数，然后转给 Service。

### 为什么要校验文档归属

Service 中有一段关键逻辑：

```java
KnowledgeDocument document = documentRepository.findById(documentId)
        .filter(item -> item.knowledgeBaseId().equals(knowledgeBaseId))
        .orElseThrow(() -> new KnowledgeDocumentNotFoundException(documentId));
```

它做了两件事：

1. 文档必须存在。
2. 文档必须属于当前知识库。

如果只根据 `documentId` 查询 chunks，而不校验 `knowledgeBaseId`，以后有权限系统后就可能出现跨知识库读取数据的问题。

这是后端安全设计里很常见的原则：子资源查询不能只校验子资源 ID，还要校验父资源归属。

### 为什么新增 DocumentChunkSummary

新增 DTO：

```java
public record DocumentChunkSummary(
        String id,
        int chunkIndex,
        String content,
        int charStart,
        int charEnd
) {
}
```

我们没有直接把 `DocumentChunk` 返回给前端。

原因是：

- DTO 是前后端契约。
- Entity 或领域对象是后端内部模型。
- 前端只需要展示 chunk 顺序、内容和字符范围，不需要知道所有内部字段。

现在看起来字段差不多，但分开后更容易维护。

### 前端 API 和 Store

前端新增接口类型：

```ts
export interface DocumentChunkSummary {
  id: string
  chunkIndex: number
  content: string
  charStart: number
  charEnd: number
}
```

新增请求函数：

```ts
export async function listDocumentChunks(knowledgeBaseId: string, documentId: string) {
  const { data } = await http.get<DocumentChunkSummary[]>(
    `/knowledge-bases/${knowledgeBaseId}/documents/${documentId}/chunks`,
  )
  return data
}
```

Store 中新增状态：

```ts
const selectedDocumentId = ref<string | null>(null)
const selectedDocumentChunks = ref<DocumentChunkSummary[]>([])
const loadingChunks = ref(false)
```

这里的职责划分是：

- 页面负责响应点击。
- Store 负责记录选中文档。
- Store 负责调用 API 加载 chunks。
- 页面负责展示 chunks。

### 为什么点击文档后才加载 chunks

文档列表只展示基础信息：

```text
filename
status
chunkCount
size
```

chunks 内容可能比较长。如果每次加载文档列表都把所有 chunks 一起返回，会让接口变重。

所以我们采用懒加载：

```text
加载文档列表
  -> 用户点击某个 READY 文档
  -> 再请求这个文档的 chunks
```

这是一种很常见的前端性能设计。

### 本节你需要掌握的知识

- REST 接口可以表达嵌套资源。
- 子资源查询要校验父资源归属。
- DTO 用来稳定前后端契约，不要随便暴露内部模型。
- 前端列表页不一定要一次加载所有详情。
- “点击后再加载详情”叫懒加载，能减少初始请求负担。
- chunk 预览是 RAG 调试能力的一部分。
