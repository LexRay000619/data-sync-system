let hot;

const fileInput = document.getElementById('file-input');
export const editBtn = document.getElementById('edit-btn');
const exportBtn = document.getElementById('export-btn');
const saveBtn = document.getElementById('save-btn');
const loadBtn = document.getElementById('load-btn');

fileInput.addEventListener('change', (event) => {
    const selectedFile = event.target.files[0];
    if (!selectedFile) {
        console.log('No file selected.');
        return;
    }
    if (selectedFile.type !== 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        alert('Invalid file type. Please select an Excel file.');
        return;
    }
    // 选择新文件后,先清屏
    document.getElementById('hot').innerHTML = "";

    const file = event.target.files[0]; // file为用户点击按钮后选择的第一个文件,其内容后续会被FileReader读取
    const reader = new FileReader();

    reader.onload = (event) => {
        const data = new Uint8Array(event.target.result);
        const workbook = XLSX.read(data, {type: 'array'});
        const sheet = workbook.Sheets[workbook.SheetNames[0]];
        const dataObject = XLSX.utils.sheet_to_json(sheet, {header: 1});    // header:1将表格的第一行各元素作为列名,否则列名将是默认的ABCD
        // dataObject是一个json对象,包含了表格的多行,每行的Key是第一行表头内容,value是本单元格内容
        hot = new Handsontable(document.getElementById('hot'), {
            data: dataObject,   // 数据源,为sheetjs生成的JavaScript对象(json)
            rowHeaders: true,   // 显示或隐藏行/列标题
            colHeaders: true,
            contextMenu: false,  // 启用或禁用表的上下文菜单(右键单元格)
            autoWrapRow: true,  // 启用或禁止文本在行/列中的自动换行
            autoWrapCol: true,
            manualColumnResize: false,   // 启用或禁止手动调整列/行的大小
            manualRowResize: false,
            colWidths: 100, // 定义行列的默认宽高
            rowHeights: 23,
            filters: false,  // 启用或禁用每列的过滤器下拉菜单
            dropdownMenu: false, // 启用或禁用每列的下拉菜单
            stretchH: 'all',    // 设置表格的水平拉伸方式,'all'表示所有列都将被拉伸以填充容器。
            readOnly: true,
            licenseKey: 'non-commercial-and-evaluation'
        });
    };

    reader.readAsArrayBuffer(file);
});

export function loadExcelData() {
    document.getElementById('hot').innerHTML = "";

    let jsonData;
    let ipAddr = window.location.hostname;
    axios.get(`http://${ipAddr}:8080/getJsonStringFromServer`).then(result => {
        console.log('The initial excel table data from the server side has been successfully obtained.');
        jsonData = result.data;
        hot = new Handsontable(document.getElementById('hot'), {
            data: jsonData,   // 数据源,为sheetjs生成的JavaScript对象(json)
            rowHeaders: true,   // 显示或隐藏行/列标题
            colHeaders: true,
            contextMenu: false,  // 启用或禁用表的上下文菜单(右键单元格)
            autoWrapRow: true,  // 启用或禁止文本在行/列中的自动换行
            autoWrapCol: true,
            manualColumnResize: false,   // 启用或禁止手动调整列/行的大小
            manualRowResize: false,
            colWidths: 100, // 定义行列的默认宽高
            rowHeights: 23,
            filters: false,  // 启用或禁用每列的过滤器下拉菜单
            dropdownMenu: false, // 启用或禁用每列的下拉菜单
            stretchH: 'all',    // 设置表格的水平拉伸方式,'all'表示所有列都将被拉伸以填充容器。
            readOnly: true,
            licenseKey: 'non-commercial-and-evaluation'
        });
        // 只有当excel文件展示在页面上之后,才能进行编辑、导出、云端保存等操作
        editBtn.disabled = false;
        saveBtn.disabled = false;
        exportBtn.disabled = false;
    });
}

loadBtn.addEventListener('click', () => {
    loadExcelData();
});

editBtn.addEventListener('click', () => {   // 切换按钮的模式,设置全表是否可编辑
    const isReadOnly = hot.getSettings().readOnly;
    hot.updateSettings({
        readOnly: !isReadOnly
    });
    const button = document.getElementById("edit-btn");
    if (button.textContent === "Edit") {
        button.textContent = "Lock";
    } else {
        button.textContent = "Edit";
    }
});

exportBtn.addEventListener('click', () => {
    const data = hot.getData(); // 从Handsontable对象中获取表格数据
    const worksheet = XLSX.utils.aoa_to_sheet(data);    // 将数据(an array of arrays)转换成XLSX库可用的工作表格式
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');    // 将工作表添加到工作簿
    const excelBuffer = XLSX.write(workbook, {bookType: 'xlsx', type: 'array'});    // 将工作簿以xlsx格式写入Excel缓冲区
    exportAsExcelFile(excelBuffer, 'myWorkbook.xlsx');
});

function exportAsExcelFile(buffer, fileName) {
    const blob = new Blob([buffer], {type: 'application/octet-stream'});    // 从缓冲区创建一个Blob对象(二进制大对象)
    const url = window.URL.createObjectURL(blob);   // 为该blob创建一个临时的URL
    const anchor = document.createElement('a'); // 创建一个新的锚点元素作为其下载属性
    anchor.href = url;
    anchor.download = fileName;
    document.body.appendChild(anchor);  // 锚元素附加到文档主体
    anchor.click();
    document.body.removeChild(anchor);  // 单击后删除锚元素
    window.URL.revokeObjectURL(url);    // 撤销临时URL以释放内存
}

saveBtn.addEventListener('click', async () => {
    let ipAddr = window.location.hostname;
    axios.post(`http://${ipAddr}:8080/saveExcelToServer`, hot.getData()).then(result => {
        console.log(result.data);
    });
});