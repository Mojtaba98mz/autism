import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'inputFilter',
})
export class InputFilterPipe implements PipeTransform {
  transform(value: any, nameSearch: string, familySearch: string): any {
    if (!nameSearch && !familySearch) {
      return value;
    }
    return value.filter(
      (item: any) =>
        item.firstName.toLowerCase().includes(nameSearch.toLowerCase()) && item.lastName.toLowerCase().includes(familySearch.toLowerCase())
    );
  }
}
