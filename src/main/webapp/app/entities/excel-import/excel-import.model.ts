export interface IExcelImport {
  id?: number;
  excelContentType?: string;
  excel?: string;
}

export class ExcelImport implements IExcelImport {
  constructor(public id?: number, public excelContentType?: string, public excel?: string) {}
}

export function getExcelImportIdentifier(excelImport: IExcelImport): number | undefined {
  return excelImport.id;
}
