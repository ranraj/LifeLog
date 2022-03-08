import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITags, getTagsIdentifier } from '../tags.model';

export type EntityResponseType = HttpResponse<ITags>;
export type EntityArrayResponseType = HttpResponse<ITags[]>;

@Injectable({ providedIn: 'root' })
export class TagsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tags: ITags): Observable<EntityResponseType> {
    return this.http.post<ITags>(this.resourceUrl, tags, { observe: 'response' });
  }

  update(tags: ITags): Observable<EntityResponseType> {
    return this.http.put<ITags>(`${this.resourceUrl}/${getTagsIdentifier(tags) as number}`, tags, { observe: 'response' });
  }

  partialUpdate(tags: ITags): Observable<EntityResponseType> {
    return this.http.patch<ITags>(`${this.resourceUrl}/${getTagsIdentifier(tags) as number}`, tags, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITags>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITags[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTagsToCollectionIfMissing(tagsCollection: ITags[], ...tagsToCheck: (ITags | null | undefined)[]): ITags[] {
    const tags: ITags[] = tagsToCheck.filter(isPresent);
    if (tags.length > 0) {
      const tagsCollectionIdentifiers = tagsCollection.map(tagsItem => getTagsIdentifier(tagsItem)!);
      const tagsToAdd = tags.filter(tagsItem => {
        const tagsIdentifier = getTagsIdentifier(tagsItem);
        if (tagsIdentifier == null || tagsCollectionIdentifiers.includes(tagsIdentifier)) {
          return false;
        }
        tagsCollectionIdentifiers.push(tagsIdentifier);
        return true;
      });
      return [...tagsToAdd, ...tagsCollection];
    }
    return tagsCollection;
  }
}
